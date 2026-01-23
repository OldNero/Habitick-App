package com.tendensee.notifications

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import com.tendensee.MainActivity
import com.tendensee.R
import com.tendensee.data.AppDatabase
import com.tendensee.data.HabitRepository
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.time.LocalDate

class ReminderReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val habitId = intent.getIntOfExtra("habitId", -1)
        val habitTitle = intent.getStringExtra("habitTitle") ?: "Habit Reminder"
        
        when (intent.action) {
            "com.tendensee.ACTION_HABIT_DONE" -> {
                if (habitId != -1) {
                    val dao = AppDatabase.getDatabase(context).habitDao()
                    val repo = HabitRepository(dao)
                    GlobalScope.launch {
                        repo.toggleHabitCompletion(habitId, LocalDate.now(), true)
                    }
                }
                // Dismiss notification
                val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                notificationManager.cancel(habitId)
            }
            else -> {
                showNotification(context, habitId, habitTitle)
            }
        }
    }

    private fun showNotification(context: Context, habitId: Int, title: String) {
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val channelId = "habit_reminders"

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(channelId, "Habit Reminders", NotificationManager.IMPORTANCE_HIGH)
            notificationManager.createNotificationChannel(channel)
        }

        val doneIntent = Intent(context, ReminderReceiver::class.java).apply {
            action = "com.tendensee.ACTION_HABIT_DONE"
            putExtra("habitId", habitId)
        }
        val donePendingIntent = PendingIntent.getBroadcast(
            context, habitId, doneIntent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val contentIntent = Intent(context, MainActivity::class.java)
        val contentPendingIntent = PendingIntent.getActivity(
            context, habitId, contentIntent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(android.R.drawable.ic_dialog_info) // Placeholder
            .setContentTitle("Time for TendenSee!")
            .setContentText("Don't forget to: $title")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(contentPendingIntent)
            .setAutoCancel(true)
            .addAction(android.R.drawable.ic_menu_edit, "Done", donePendingIntent)
            .build()

        notificationManager.notify(habitId, notification)
    }
}

// Helper extension because getIntExtra returns default, but for better clarity
fun Intent.getIntOfExtra(name: String, defaultValue: Int): Int = getIntExtra(name, defaultValue)
