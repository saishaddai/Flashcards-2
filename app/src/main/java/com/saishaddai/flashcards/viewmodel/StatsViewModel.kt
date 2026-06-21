package com.saishaddai.flashcards.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.saishaddai.flashcards.repository.StatsRepository
import com.saishaddai.flashcards.screens.MasteryData
import com.saishaddai.flashcards.utils.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

data class StatsUiState(
    val weeklyActivity: List<Int> = emptyList(),
    val skillMastery: List<MasteryData> = emptyList(),
    val flashcardsViewed: String = "0",
    val currentStreak: String = "0 Days",
    val studyTime: String = "0h",
    val masteredDecks: String = "0%",
    val weeklyComparison: Int = 0,
    val isSkillsExpanded: Boolean = false,
    val infoDialogContent: Pair<String, String>? = null
)

class StatsViewModel(
    private val repository: StatsRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState<StatsUiState>>(UiState.Loading)
    val uiState: StateFlow<UiState<StatsUiState>> = _uiState.asStateFlow()

    init {
        loadStats()
    }

    fun loadStats() {
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            
            combine(
                repository.getWeeklyActivity(),
                repository.getSkillMastery(),
                repository.getFlashcardsViewed(),
                repository.getCurrentStreak(),
                repository.getStudyTime(),
                repository.getMasteredDecks(),
                repository.getWeeklyComparison()
            ) { results ->
                StatsUiState(
                    weeklyActivity = results[0] as List<Int>,
                    skillMastery = results[1] as List<MasteryData>,
                    flashcardsViewed = results[2] as String,
                    currentStreak = results[3] as String,
                    studyTime = results[4] as String,
                    masteredDecks = results[5] as String,
                    weeklyComparison = results[6] as Int
                )
            }.catch { e ->
                _uiState.value = UiState.Error("Failed to load statistics", e)
            }.collect { stats ->
                _uiState.value = UiState.Success(stats)
            }
        }
    }

    fun onViewAllSkillsClicked() {
        val currentState = _uiState.value
        if (currentState is UiState.Success) {
            _uiState.value = UiState.Success(
                currentState.data.copy(isSkillsExpanded = !currentState.data.isSkillsExpanded)
            )
        }
    }

    fun onInfoClick(title: String, description: String) {
        val currentState = _uiState.value
        if (currentState is UiState.Success) {
            _uiState.value = UiState.Success(
                currentState.data.copy(infoDialogContent = title to description)
            )
        }
    }

    fun onDismissInfoDialog() {
        val currentState = _uiState.value
        if (currentState is UiState.Success) {
            _uiState.value = UiState.Success(
                currentState.data.copy(infoDialogContent = null)
            )
        }
    }
}
