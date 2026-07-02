package com.saishaddai.flashcards.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.saishaddai.flashcards.repository.SettingsRepository
import com.saishaddai.flashcards.repository.UserSettings
import com.saishaddai.flashcards.utils.UiState
import com.saishaddai.flashcards.worker.WorkerUtils
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.Locale

data class SettingsUiData(
    val userSettings: UserSettings,
    val isActionLoading: Boolean = false
)

sealed class SettingsEvent {
    data class ShowSnackbar(val message: String) : SettingsEvent()
}

@OptIn(FlowPreview::class)
class SettingsViewModel(
    application: Application,
    private val repository: SettingsRepository
) : AndroidViewModel(application) {

    private val _isActionLoading = MutableStateFlow(false)
    private val _events = MutableSharedFlow<SettingsEvent>()
    val events = _events.asSharedFlow()

    private val _rescheduleRemindersRequest = MutableSharedFlow<Unit>()

    val uiState: StateFlow<UiState<SettingsUiData>> = combine(
        repository.getSettings(),
        _isActionLoading
    ) { settings, loading ->
        SettingsUiData(settings, loading)
    }.map { data ->
        UiState.Success(data) as UiState<SettingsUiData>
    }.catch { e ->
        emit(UiState.Error("Failed to load settings", e))
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = UiState.Loading
    )

    val userSettings: StateFlow<UserSettings?> = repository.getSettings()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = null
        )

    init {
        viewModelScope.launch {
            _rescheduleRemindersRequest.debounce(1000L).collect {
                val settings = repository.getSettings().first()
                if (settings.studyReminders) {
                    WorkerUtils.scheduleDailyReminder(getApplication(), settings.preferredStudyTime)
                    _events.emit(SettingsEvent.ShowSnackbar("Reminder scheduled for ${settings.preferredStudyTime}"))
                } else {
                    WorkerUtils.cancelDailyReminder(getApplication())
                    _events.emit(SettingsEvent.ShowSnackbar("Reminders disabled"))
                }
            }
        }
    }


    fun onRestartMasteryClicked() {
        viewModelScope.launch {
            _isActionLoading.value = true
            try {
                repository.restartMasteryExperience()
            } catch (e: Exception) {
                // Settings usually just fail to load, action failures are currently silent or logged
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
            _rescheduleRemindersRequest.emit(Unit)
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
            _rescheduleRemindersRequest.emit(Unit)
        }
    }

    fun onNotificationSoundChanged(enabled: Boolean) {
        viewModelScope.launch {
            repository.saveNotificationSound(enabled)
        }
    }
}
