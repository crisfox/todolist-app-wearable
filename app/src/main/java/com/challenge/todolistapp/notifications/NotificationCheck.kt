package com.challenge.todolistapp.notifications

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.widget.Toast
import androidx.core.app.NotificationManagerCompat

fun checkNotificationSettings(context: Context) {
    val notificationManager = NotificationManagerCompat.from(context)
    if (!notificationManager.areNotificationsEnabled()) {
        // Notifications are disabled
        Toast.makeText(context, "Notifications are disabled. Please enable them in settings.", Toast.LENGTH_LONG).show()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Redirect to notification settings if API level is 26 or higher
            val intent = Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS).apply {
                putExtra(Settings.EXTRA_APP_PACKAGE, context.packageName)
            }
            context.startActivity(intent)
        } else {
            // For older versions, redirect to general app settings
            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                data = Uri.fromParts("package", context.packageName, null)
            }
            context.startActivity(intent)
        }
    } else {
        // Notifications are enabled
        Toast.makeText(context, "Notifications are enabled.", Toast.LENGTH_SHORT).show()
    }
}