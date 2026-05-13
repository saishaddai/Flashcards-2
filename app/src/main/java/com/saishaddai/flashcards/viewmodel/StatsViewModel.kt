package com.saishaddai.flashcards.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.saishaddai.flashcards.repository.StatsRepository
import com.saishaddai.flashcards.repository.impl.HardcodedStatsRepository
import com.saishaddai.flashcards.screens.MasteryData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class StatsViewModel(
    private val repository: StatsRepository = HardcodedStatsRepository()
) : ViewModel() {

    private val _weeklyActivity = MutableStateFlow<List<Int>>(emptyList())
    val weeklyActivity: StateFlow<List<Int>> = _weeklyActivity.asStateFlow()

    private val _skillMastery = MutableStateFlow<List<MasteryData>>(emptyList())
    val skillMastery: StateFlow<List<MasteryData>> = _skillMastery.asStateFlow()

    private val _flashcardsViewed = MutableStateFlow("0")
    val flashcardsViewed: StateFlow<String> = _flashcardsViewed.asStateFlow()

    private val _currentStreak = MutableStateFlow("0 Days")
    val currentStreak: StateFlow<String> = _currentStreak.asStateFlow()

    private val _studyTime = MutableStateFlow("0h")
    val studyTime: StateFlow<String> = _studyTime.asStateFlow()

    private val _masteredDecks = MutableStateFlow("0%")
    val masteredDecks: StateFlow<String> = _masteredDecks.asStateFlow()

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    init {
        loadStats()
    }

    private fun loadStats() {
        viewModelScope.launch {
            repository.getWeeklyActivity().collect { 
                _weeklyActivity.value = it
                checkLoadingFinished()
            }
        }
        viewModelScope.launch {
            repository.getSkillMastery().collect { 
                _skillMastery.value = it
                checkLoadingFinished()
            }
        }
        viewModelScope.launch {
            repository.getFlashcardsViewed().collect {
                _flashcardsViewed.value = it
                checkLoadingFinished()
            }
        }
        viewModelScope.launch {
            repository.getCurrentStreak().collect { 
                _currentStreak.value = it
                checkLoadingFinished()
            }
        }
        viewModelScope.launch {
            repository.getStudyTime().collect { 
                _studyTime.value = it
                checkLoadingFinished()
            }
        }
        viewModelScope.launch {
            repository.getMasteredDecks().collect {
                _masteredDecks.value = it
                checkLoadingFinished()
            }
        }
    }

    private fun checkLoadingFinished() {
        // In a real app, we might wait for all flows to emit at least once.
        // For this exercise, we can just set it to false after some data is loaded or use a delay.
        // Let's assume once we have skillMastery and weeklyActivity, we are good enough or just simple logic.
        if (_skillMastery.value.isNotEmpty()) {
            _isLoading.value = false
        }
    }

    fun onViewAllSkillsClicked() {
        // Handle view all skills logic
    }
}
