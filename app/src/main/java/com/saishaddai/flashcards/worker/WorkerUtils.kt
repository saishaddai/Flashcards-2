package com.saishaddai.flashcards.worker

import android.content.Context
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import java.util.Calendar
import java.util.concurrent.TimeUnit

object WorkerUtils {
    private const val REMINDER_WORK_NAME = "daily_reminder_work"

    fun scheduleDailyReminder(context: Context, preferredTime: String) {
        val workManager = WorkManager.getInstance(context)

        // preferredTime format is "HH:mm AM/PM" (e.g., "09:00 PM")
        val (hour, minute) = parseTime(preferredTime)

        val calendar = Calendar.getInstance()
        val now = calendar.timeInMillis

        calendar.set(Calendar.HOUR_OF_DAY, hour)
        calendar.set(Calendar.MINUTE, minute)
        calendar.set(Calendar.SECOND, 0)

        // If time is in the past, schedule for tomorrow
        if (calendar.timeInMillis <= now) {
            calendar.add(Calendar.DAY_OF_YEAR, 1)
        }

        val initialDelay = calendar.timeInMillis - now

        val workRequest = PeriodicWorkRequestBuilder<ReminderWorker>(24, TimeUnit.HOURS)
            .setInitialDelay(initialDelay, TimeUnit.MILLISECONDS)
            .build()

        workManager.enqueueUniquePeriodicWork(
            REMINDER_WORK_NAME,
            ExistingPeriodicWorkPolicy.UPDATE,
            workRequest
        )
    }

    fun cancelDailyReminder(context: Context) {
        WorkManager.getInstance(context).cancelUniqueWork(REMINDER_WORK_NAME)
    }

    private fun parseTime(time: String): Pair<Int, Int> {
        val parts = time.split(" ")
        val timeParts = parts[0].split(":")
        var hour = timeParts[0].toInt()
        val minute = timeParts[1].toInt()
        val amPm = parts[1]

        if (amPm == "PM" && hour < 12) hour += 12
        if (amPm == "AM" && hour == 12) hour = 0

        return Pair(hour, minute)
    }
}
