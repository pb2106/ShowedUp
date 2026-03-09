package com.showedup.app.scheduler

import android.content.Context
import androidx.work.ExistingWorkPolicy
import androidx.work.WorkManager
import com.showedup.app.data.repository.ScheduleRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import android.util.Log
import java.time.LocalDate
import java.time.LocalTime
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AttendanceScheduler @Inject constructor(
    @ApplicationContext private val context: Context,
    private val scheduleRepository: ScheduleRepository
) {
    suspend fun scheduleForToday() {
        val workManager = WorkManager.getInstance(context)
        
        // Cancel all existing attendance work for today to avoid duplicates if classes change
        workManager.cancelAllWorkByTag("attendance_today")
        
        val today = LocalDate.now()
        val now = LocalTime.now()
        val nowMinutes = now.hour * 60 + now.minute
        
        Log.d("AttendanceScheduler", "Tracking attendance. Current time: $now (minutes: $nowMinutes)")
        
        // Get today's classes synchronously
        val classes = scheduleRepository.getActiveClassesByDaySnapshot(today.dayOfWeek)
        
        for (entry in classes) {
            // Schedule exactly at class start time. 
            if (entry.startTimeMinutes > nowMinutes) {
                val delayMinutes = entry.startTimeMinutes - nowMinutes
                val delayMs = delayMinutes * 60 * 1000L
                
                Log.d("AttendanceScheduler", "Scheduling class ${entry.courseName} (Start: ${entry.startTimeMinutes}m) with delay $delayMinutes mins ($delayMs ms)")
                
                val request = AttendanceTriggerWorker.createWorkRequest(
                    entryId = entry.id,
                    courseName = entry.courseName,
                    date = today.toString(),
                    delayMs = delayMs
                )
                
                workManager.enqueueUniqueWork(
                    "attendance_today_${entry.id}",
                    ExistingWorkPolicy.REPLACE,
                    request
                )
            } else {
                Log.d("AttendanceScheduler", "Class ${entry.courseName} (Start: ${entry.startTimeMinutes}m) is already in the past. Skipping.")
            }
        }
    }
}
