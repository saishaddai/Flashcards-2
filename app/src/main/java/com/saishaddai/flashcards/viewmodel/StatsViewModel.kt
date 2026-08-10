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

data class StatsUiData(
    val weeklyActivity: List<Int> = emptyList(),
    val skillMastery: List<MasteryData> = emptyList(),
    val flashcardsViewed: String = "0",
    val currentStreak: String = "0 Days",
    val studyTime: String = "0m",
    val masteredDecks: String = "0%",
    val weeklyComparison: Int = 0,
    val isSkillsExpanded: Boolean = false,
    val infoDialogContent: Pair<String, String>? = null
)

class StatsViewModel(
    private val repository: StatsRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState<StatsUiData>>(UiState.Loading)
    val uiState: StateFlow<UiState<StatsUiData>> = _uiState.asStateFlow()

    init {
        loadStats()
    }

    fun loadStats() {
        viewModelScope.launch {
            _uiState.value = UiState.Loading

            val generalStatsFlow = combine(
                repository.getFlashcardsViewed(),
                repository.getCurrentStreak(),
                repository.getStudyTime(),
                repository.getMasteredDecks(),
                repository.getWeeklyComparison()
            ) { viewed, streak, time, mastered, comparison ->
                GeneralStats(viewed, streak, time, mastered, comparison)
            }

            combine(
                repository.getWeeklyActivity(),
                repository.getSkillMastery(),
                generalStatsFlow
            ) { weekly, skills, general ->
                StatsUiData(
                    weeklyActivity = weekly,
                    skillMastery = skills,
                    flashcardsViewed = general.viewed,
                    currentStreak = general.streak,
                    studyTime = general.time,
                    masteredDecks = general.mastered,
                    weeklyComparison = general.comparison
                )
            }.catch { e ->
                _uiState.value = UiState.Error("Failed to load statistics", e)
            }.collect { stats ->
                _uiState.value = UiState.Success(stats)
            }
        }
    }

    private data class GeneralStats(
        val viewed: String,
        val streak: String,
        val time: String,
        val mastered: String,
        val comparison: Int
    )

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
