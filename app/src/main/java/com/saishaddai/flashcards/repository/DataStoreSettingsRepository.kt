package com.saishaddai.flashcards.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class DataStoreSettingsRepository(private val context: Context) : SettingsRepository {

    private object PreferencesKeys {
        val FLASHCARDS_PER_SESSION = intPreferencesKey("flashcards_per_session")
        val DAILY_STUDY_GOAL = intPreferencesKey("daily_study_goal")
        val DARK_MODE = booleanPreferencesKey("dark_mode")
        val STUDY_REMINDERS = booleanPreferencesKey("study_reminders")
        val NOTIFICATION_SOUND = booleanPreferencesKey("notification_sound")
        val PREFERRED_STUDY_TIME = stringPreferencesKey("preferred_study_time")
        val QUICK_START = booleanPreferencesKey("quick_start")
        val SHOW_ANSWERS = booleanPreferencesKey("show_answers")
        val SHOW_SUGGESTIONS = booleanPreferencesKey("show_suggestions")
    }

    val settingsFlow: Flow<UserSettings> = context.dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { preferences ->
            UserSettings(
                flashcardsPerSession = preferences[PreferencesKeys.FLASHCARDS_PER_SESSION] ?: 20,
                dailyStudyGoal = preferences[PreferencesKeys.DAILY_STUDY_GOAL] ?: 50,
                isDarkMode = preferences[PreferencesKeys.DARK_MODE] ?: true,
                studyReminders = preferences[PreferencesKeys.STUDY_REMINDERS] ?: true,
                notificationSound = preferences[PreferencesKeys.NOTIFICATION_SOUND] ?: false,
                preferredStudyTime = preferences[PreferencesKeys.PREFERRED_STUDY_TIME] ?: "09:00 PM",
                quickStart = preferences[PreferencesKeys.QUICK_START] ?: true,
                showAnswers = preferences[PreferencesKeys.SHOW_ANSWERS] ?: true,
                showSuggestions = preferences[PreferencesKeys.SHOW_SUGGESTIONS] ?: true
            )
        }

    override suspend fun restartMasteryExperience() {
        context.dataStore.edit { it.clear() }
    }

    override suspend fun saveFlashcardsPerSession(count: Int) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.FLASHCARDS_PER_SESSION] = count
        }
    }

    override suspend fun saveDailyStudyGoal(count: Int) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.DAILY_STUDY_GOAL] = count
        }
    }

    override suspend fun saveDarkMode(enabled: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.DARK_MODE] = enabled
        }
    }

    override suspend fun saveStudyReminders(enabled: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.STUDY_REMINDERS] = enabled
        }
    }

    override suspend fun saveNotificationSound(enabled: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.NOTIFICATION_SOUND] = enabled
        }
    }

    suspend fun savePreferredStudyTime(time: String) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.PREFERRED_STUDY_TIME] = time
        }
    }

    suspend fun saveQuickStart(enabled: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.QUICK_START] = enabled
        }
    }

    suspend fun saveShowAnswers(enabled: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.SHOW_ANSWERS] = enabled
        }
    }

    suspend fun saveShowSuggestions(enabled: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.SHOW_SUGGESTIONS] = enabled
        }
    }
}

data class UserSettings(
    val flashcardsPerSession: Int,
    val dailyStudyGoal: Int,
    val isDarkMode: Boolean,
    val studyReminders: Boolean,
    val notificationSound: Boolean,
    val preferredStudyTime: String,
    val quickStart: Boolean,
    val showAnswers: Boolean,
    val showSuggestions: Boolean
)
