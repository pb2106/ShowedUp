package com.showedup.app.signal.service

import android.app.Notification
import android.app.Service
import android.content.Intent
import android.content.pm.ServiceInfo
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.showedup.app.R
import com.showedup.app.ShowedUpApplication
import com.showedup.app.data.repository.AttendanceRepository
import com.showedup.app.data.repository.SecurityRepository
import com.showedup.app.data.entity.SecurityEventType
import com.showedup.app.signal.MultiModalSignalCollector
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import javax.inject.Inject

/**
 * Foreground service for signal collection.
 * Shows a countdown notification while collecting.
 */
@AndroidEntryPoint
class SignalCollectionService : Service() {

    @Inject lateinit var signalCollector: MultiModalSignalCollector
    @Inject lateinit var attendanceRepository: AttendanceRepository
    @Inject lateinit var securityRepository: SecurityRepository

    private val serviceScope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    companion object {
        const val EXTRA_TIMETABLE_ENTRY_ID = "timetable_entry_id"
        const val EXTRA_COURSE_NAME = "course_name"
        const val EXTRA_DATE = "date"
        const val ACTION_COLLECT = "com.showedup.app.ACTION_COLLECT"
        private const val NOTIFICATION_ID = 1001
    }

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val notification = createNotification()
        startForeground(
            NOTIFICATION_ID,
            notification,
            ServiceInfo.FOREGROUND_SERVICE_TYPE_MICROPHONE or
                    ServiceInfo.FOREGROUND_SERVICE_TYPE_LOCATION
        )

        val entryId = intent?.getLongExtra(EXTRA_TIMETABLE_ENTRY_ID, -1) ?: -1
        val courseName = intent?.getStringExtra(EXTRA_COURSE_NAME) ?: ""
        val date = intent?.getStringExtra(EXTRA_DATE) ?: ""

        if (entryId == -1L) {
            stopSelf()
            return START_NOT_STICKY
        }

        serviceScope.launch {
            try {
                securityRepository.logEvent(SecurityEventType.SIGNAL_COLLECTION_STARTED)
                val result = signalCollector.collectAll()

                attendanceRepository.insertRecord(
                    timetableEntryId = entryId,
                    courseName = courseName,
                    date = date,
                    gpsHash = result.gpsHash,
                    wifiHash = result.wifiHash,
                    bluetoothHash = result.bluetoothHash,
                    audioHash = result.audioHash,
                    sensorHash = result.sensorHash
                )
                securityRepository.logEvent(SecurityEventType.SIGNAL_COLLECTION_COMPLETED)
            } catch (e: Exception) {
                securityRepository.logEvent(
                    SecurityEventType.SIGNAL_COLLECTION_FAILED,
                    e.message ?: "Unknown error"
                )
            } finally {
                stopSelf()
            }
        }

        return START_NOT_STICKY
    }

    override fun onDestroy() {
        serviceScope.cancel()
        super.onDestroy()
    }

    private fun createNotification(): Notification {
        return NotificationCompat.Builder(this, ShowedUpApplication.CHANNEL_SIGNAL_COLLECTION)
            .setContentTitle(getString(R.string.signal_collecting))
            .setSmallIcon(android.R.drawable.ic_popup_sync)
            .setOngoing(true)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .build()
    }
}
