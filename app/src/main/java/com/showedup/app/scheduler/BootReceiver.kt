package com.showedup.app.scheduler

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.work.WorkManager
import com.showedup.app.data.dao.TimetableDao
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalTime
import javax.inject.Inject

/**
 * Reschedules all attendance jobs on BOOT_COMPLETED.
 */
@AndroidEntryPoint
class BootReceiver : BroadcastReceiver() {

    @Inject lateinit var attendanceScheduler: AttendanceScheduler

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action != Intent.ACTION_BOOT_COMPLETED) return

        val pendingResult = goAsync()
        CoroutineScope(Dispatchers.IO).launch {
            try {
                rescheduleAll(context)
            } finally {
                pendingResult.finish()
            }
        }
    }

    private suspend fun rescheduleAll(context: Context) {
        attendanceScheduler.scheduleForToday()
    }
}
