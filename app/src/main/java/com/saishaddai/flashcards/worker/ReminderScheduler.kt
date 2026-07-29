package com.saishaddai.flashcards.worker

import android.content.Context

interface ReminderScheduler {
    fun scheduleDailyReminder(preferredTime: String)
    fun cancelDailyReminder()
}

class WorkManagerReminderScheduler(private val context: Context) : ReminderScheduler {
    override fun scheduleDailyReminder(preferredTime: String) {
        WorkerUtils.scheduleDailyReminder(context, preferredTime)
    }

    override fun cancelDailyReminder() {
        WorkerUtils.cancelDailyReminder(context)
    }
}
