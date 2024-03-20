package com.example.practica01.objects

import android.Manifest
import android.app.PendingIntent
import android.app.TaskStackBuilder
import android.content.pm.PackageManager
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.practica01.EditNotificationScreen
import com.example.practica01.R
import androidx.core.app.ActivityCompat
import android.content.Context
import android.content.Intent

object Notifications {
    fun showNotification(context: Context, title: String, content: String) {

        var builder = NotificationCompat.Builder(context, "Notificaciones")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle(title)
            .setContentText(content)
            .setPriority(NotificationCompat.PRIORITY_LOW)

        with(NotificationManagerCompat.from(context)) {
            if(ActivityCompat.checkSelfPermission(
                    context,
                    Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED
            ) {
                return@with
            }
            notify(1, builder.build())
        }
    }

    fun showNotificationWithIntent(context: Context, title: String, content: String, intent: Intent) {
        val resultPendingIntent: PendingIntent? = TaskStackBuilder.create(context).run {
            addNextIntentWithParentStack(intent)
            getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)
        }

        val builder = NotificationCompat.Builder(context, "Notificaciones").apply {
            setSmallIcon(R.drawable.ic_launcher_foreground)
            setContentTitle(title)
            setContentText(content)
            setPriority(NotificationCompat.PRIORITY_LOW)
            setContentIntent(resultPendingIntent)
        }
        with(NotificationManagerCompat.from(context)) {
            if (ActivityCompat.checkSelfPermission(
                    context,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                return
            }
            notify(1, builder.build())
        }
    }
}