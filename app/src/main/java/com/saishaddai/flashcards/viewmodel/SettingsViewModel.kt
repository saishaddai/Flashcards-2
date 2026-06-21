package com.saishaddai.flashcards.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.saishaddai.flashcards.repository.SettingsRepository
import com.saishaddai.flashcards.repository.UserSettings
import com.saishaddai.flashcards.utils.UiState
import com.saishaddai.flashcards.worker.WorkerUtils
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.Locale

data class SettingsUiData(
    val userSettings: UserSettings,
    val isActionLoading: Boolean = false
)

class SettingsViewModel(
    application: Application,
    private val repository: SettingsRepository
) : AndroidViewModel(application) {

    private val _isActionLoading = MutableStateFlow(false)

    val uiState: StateFlow<UiState<SettingsUiData>> = combine(
        repository.getSettings(),
        _isActionLoading
    ) { settings, loading ->
        UiState.Success(SettingsUiData(settings, loading)) as UiState<SettingsUiData>
    }.catch { e ->
        emit(UiState.Error("Failed to load settings", e))
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = UiState.Loading
    )

    fun onRestartMasteryClicked() {
        viewModelScope.launch {
            _isActionLoading.value = true
            try {
                repository.restartMasteryExperience()
            } catch (e: Exception) {
                // We might want a separate error state for actions, 
                // but for now we'll just log or show a global error if it's critical.
            } finally {
                _isActionLoading.value = false
            }
        }
    }

    fun onPreferredStudyTimeChanged(hour: Int, minute: Int) {
        viewModelScope.launch {
            val amPm = if (hour < 12) "AM" else "PM"
            val hourFormatted = if (hour % 12 == 0) 12 else hour % 12
            val time = String.format(Locale.getDefault(), "%02d:%02d %s", hourFormatted, minute, amPm)
            repository.savePreferredStudyTime(time)

            // Update worker if reminders are enabled
            val settings = repository.getSettings().first()
            if (settings.studyReminders) {
                WorkerUtils.scheduleDailyReminder(getApplication(), time)
            }
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
            if (enabled) {
                val settings = repository.getSettings().first()
                WorkerUtils.scheduleDailyReminder(getApplication(), settings.preferredStudyTime)
            } else {
                WorkerUtils.cancelDailyReminder(getApplication())
            }
        }
    }

    fun onNotificationSoundChanged(enabled: Boolean) {
        viewModelScope.launch {
            repository.saveNotificationSound(enabled)
        }
    }
}
