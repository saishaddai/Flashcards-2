package com.saishaddai.flashcards.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.saishaddai.flashcards.repository.SettingsRepository
import com.saishaddai.flashcards.repository.UserSettings
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.Locale

class SettingsViewModel(
    private val repository: SettingsRepository
) : ViewModel() {

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    val userSettings: StateFlow<UserSettings?> = repository.getSettings()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = null
        )

    fun onRestartMasteryClicked() {
        viewModelScope.launch {
            _isLoading.value = true
            repository.restartMasteryExperience()
            _isLoading.value = false
        }
    }

    fun onPreferredStudyTimeChanged(hour: Int, minute: Int) {
        viewModelScope.launch {
            val amPm = if (hour < 12) "AM" else "PM"
            val hourFormatted = if (hour % 12 == 0) 12 else hour % 12
            val time = String.format(Locale.getDefault(), "%02d:%02d %s", hourFormatted, minute, amPm)
            repository.savePreferredStudyTime(time)
        }
    }

    fun onFlashcardsPerSessionChanged(count: Int) {
        viewModelScope.launch {
            repository.saveFlashcardsPerSession(count)
        }
    }

    fun onDailyStudyGoalChanged(count: Int) {
        viewModelScope.launch {
            repository.saveDailyStudyGoal(count)
        }
    }

    fun onQuickStartChanged(enabled: Boolean) {
        viewModelScope.launch {
            repository.saveQuickStart(enabled)
        }
    }

    fun onShowAnswersChanged(enabled: Boolean) {
        viewModelScope.launch {
            repository.saveShowAnswers(enabled)
        }
    }

    fun onShowSuggestionsChanged(enabled: Boolean) {
        viewModelScope.launch {
            repository.saveShowSuggestions(enabled)
        }
    }

    fun onStudyRemindersChanged(enabled: Boolean) {
        viewModelScope.launch {
            repository.saveStudyReminders(enabled)
        }
    }

    fun onNotificationSoundChanged(enabled: Boolean) {
        viewModelScope.launch {
            repository.saveNotificationSound(enabled)
        }
    }
}
