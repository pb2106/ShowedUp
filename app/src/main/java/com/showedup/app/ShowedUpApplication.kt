package com.showedup.app

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class ShowedUpApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        createNotificationChannels()
    }

    private fun createNotificationChannels() {
        val channel = NotificationChannel(
            CHANNEL_SIGNAL_COLLECTION,
            getString(R.string.notification_channel_signal),
            NotificationManager.IMPORTANCE_LOW
        ).apply {
            description = "Notifications shown while recording attendance signals"
            setShowBadge(false)
        }

        val nm = getSystemService(NotificationManager::class.java)
        nm.createNotificationChannel(channel)
    }

    companion object {
        const val CHANNEL_SIGNAL_COLLECTION = "signal_collection"
    }
}
