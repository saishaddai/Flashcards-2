package com.saishaddai.flashcards.repository

import kotlinx.coroutines.flow.Flow

interface SettingsRepository {
    suspend fun restartMasteryExperience()
    suspend fun saveFlashcardsPerSession(count: Int)
    suspend fun saveDailyStudyGoal(count: Int)
    suspend fun saveDarkMode(enabled: Boolean)
    suspend fun saveStudyReminders(enabled: Boolean)
    suspend fun saveNotificationSound(enabled: Boolean)
    suspend fun savePreferredStudyTime(time: String)
    suspend fun saveQuickStart(enabled: Boolean)
    suspend fun saveShowAnswers(enabled: Boolean)
    suspend fun saveShowSuggestions(enabled: Boolean)
    fun getSettings(): Flow<UserSettings>
}

data class UserSettings(
    val flashcardsPerSession: Int = 20,
    val dailyStudyGoal: Int = 50,
    val isDarkMode: Boolean = true,
    val studyReminders: Boolean = true,
    val notificationSound: Boolean = false,
    val preferredStudyTime: String = "09:00 PM",
    val quickStart: Boolean = false,
    val showAnswers: Boolean = false,
    val showSuggestions: Boolean = true
)
