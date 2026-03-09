package com.showedup.app.scheduler

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.*
import com.showedup.app.data.repository.AttendanceRepository
import com.showedup.app.data.repository.ScheduleRepository
import com.showedup.app.signal.MultiModalSignalCollector
import com.showedup.app.data.repository.SecurityRepository
import com.showedup.app.data.entity.SecurityEventType
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import java.time.LocalDate
import java.time.DayOfWeek

/**
 * WorkManager worker for scheduled attendance triggers.
 * Implements suppression chain: Calendar → DayOff → WeeklySchedule → Fire.
 */
@HiltWorker
class AttendanceTriggerWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted workerParams: WorkerParameters,
    private val scheduleRepository: ScheduleRepository,
    private val attendanceRepository: AttendanceRepository,
    private val signalCollector: MultiModalSignalCollector,
    private val securityRepository: SecurityRepository
) : CoroutineWorker(appContext, workerParams) {

    companion object {
        const val KEY_TIMETABLE_ENTRY_ID = "timetable_entry_id"
        const val KEY_COURSE_NAME = "course_name"
        const val KEY_DATE = "date"

        fun createWorkRequest(
            entryId: Long,
            courseName: String,
            date: String,
            delayMs: Long
        ): OneTimeWorkRequest {
            val data = Data.Builder()
                .putLong(KEY_TIMETABLE_ENTRY_ID, entryId)
                .putString(KEY_COURSE_NAME, courseName)
                .putString(KEY_DATE, date)
                .build()

            return OneTimeWorkRequestBuilder<AttendanceTriggerWorker>()
                .setInputData(data)
                .setInitialDelay(java.time.Duration.ofMillis(delayMs))
                .addTag("attendance_$entryId")
                .addTag("attendance_today")
                .build()
        }
    }

    override suspend fun doWork(): Result {
        val entryId = inputData.getLong(KEY_TIMETABLE_ENTRY_ID, -1)
        val courseName = inputData.getString(KEY_COURSE_NAME) ?: return Result.failure()
        val date = inputData.getString(KEY_DATE) ?: LocalDate.now().toString()

        if (entryId == -1L) return Result.failure()

        // Suppression chain check
        if (shouldSuppress(date)) {
            return Result.success() // silently suppressed
        }

        return try {
            securityRepository.logEvent(SecurityEventType.SIGNAL_COLLECTION_STARTED)
            val signals = signalCollector.collectAll()

            attendanceRepository.insertRecord(
                timetableEntryId = entryId,
                courseName = courseName,
                date = date,
                gpsHash = signals.gpsHash,
                wifiHash = signals.wifiHash,
                bluetoothHash = signals.bluetoothHash,
                audioHash = signals.audioHash,
                sensorHash = signals.sensorHash
            )
            securityRepository.logEvent(SecurityEventType.SIGNAL_COLLECTION_COMPLETED)
            Result.success()
        } catch (e: Exception) {
            securityRepository.logEvent(
                SecurityEventType.SIGNAL_COLLECTION_FAILED,
                e.message ?: "Unknown error"
            )
            Result.retry()
        }
    }

    private suspend fun shouldSuppress(date: String): Boolean {
        // Check day-off records
        val dayOffs = attendanceRepository.getDayOffsByDate(date)
        if (dayOffs.isNotEmpty()) return true

        // Check weekly schedule
        val today = LocalDate.parse(date)
        val schedule = scheduleRepository.getLatestWeeklyScheduleSnapshot()
        if (schedule != null) {
            val activeDays = schedule.activeDays.split(",")
                .filter { it.isNotBlank() }
                .map { DayOfWeek.of(it.trim().toInt()) }
            if (today.dayOfWeek !in activeDays) return true
        }

        return false
    }
}
