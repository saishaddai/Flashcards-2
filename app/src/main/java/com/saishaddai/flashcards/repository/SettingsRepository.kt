package com.saishaddai.flashcards.repository

interface SettingsRepository {
    suspend fun restartMasteryExperience()
    suspend fun saveFlashcardsPerSession(count: Int)
    suspend fun saveDailyStudyGoal(count: Int)
    suspend fun saveDarkMode(enabled: Boolean)
    suspend fun saveStudyReminders(enabled: Boolean)
    suspend fun saveNotificationSound(enabled: Boolean)
}
