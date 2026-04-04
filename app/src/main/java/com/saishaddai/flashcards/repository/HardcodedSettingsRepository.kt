package com.saishaddai.flashcards.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext

class HardcodedSettingsRepository : SettingsRepository {
    override suspend fun restartMasteryExperience() = withContext(Dispatchers.IO) {
        // Simulation of a long-running reset process
        delay(1000)
        // In a real app, this would clear databases, DataStore, or SharedPreferences.
    }

    override suspend fun saveFlashcardsPerSession(count: Int) {
        delay(1000)
    }

    override suspend fun saveDailyStudyGoal(count: Int) {
        delay(1000)
    }

    override suspend fun saveDarkMode(enabled: Boolean) {
        delay(1000)
    }

    override suspend fun saveStudyReminders(enabled: Boolean) {
        delay(1000)
    }

    override suspend fun saveNotificationSound(enabled: Boolean) {
        delay(1000)
    }
}
