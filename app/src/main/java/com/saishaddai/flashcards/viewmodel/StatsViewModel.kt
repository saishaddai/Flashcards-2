package com.saishaddai.flashcards.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.saishaddai.flashcards.repository.StatsRepository
import com.saishaddai.flashcards.screens.MasteryData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class StatsViewModel(
    private val repository: StatsRepository
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

    private var weeklyActivityLoaded = false
    private var skillMasteryLoaded = false
    private var flashcardsViewedLoaded = false
    private var currentStreakLoaded = false
    private var studyTimeLoaded = false
    private var masteredDecksLoaded = false

    init {
        loadStats()
    }

    private fun loadStats() {
        viewModelScope.launch {
            repository.getWeeklyActivity().collect { 
                _weeklyActivity.value = it
                weeklyActivityLoaded = true
                checkLoadingFinished()
            }
        }
        viewModelScope.launch {
            repository.getSkillMastery().collect { 
                _skillMastery.value = it
                skillMasteryLoaded = true
                checkLoadingFinished()
            }
        }
        viewModelScope.launch {
            repository.getFlashcardsViewed().collect {
                _flashcardsViewed.value = it
                flashcardsViewedLoaded = true
                checkLoadingFinished()
            }
        }
        viewModelScope.launch {
            repository.getCurrentStreak().collect { 
                _currentStreak.value = it
                currentStreakLoaded = true
                checkLoadingFinished()
            }
        }
        viewModelScope.launch {
            repository.getStudyTime().collect { 
                _studyTime.value = it
                studyTimeLoaded = true
                checkLoadingFinished()
            }
        }
        viewModelScope.launch {
            repository.getMasteredDecks().collect {
                _masteredDecks.value = it
                masteredDecksLoaded = true
                checkLoadingFinished()
            }
        }
    }

    private fun checkLoadingFinished() {
        if (weeklyActivityLoaded && skillMasteryLoaded && flashcardsViewedLoaded && 
            currentStreakLoaded && studyTimeLoaded && masteredDecksLoaded) {
            _isLoading.value = false
        }
    }

    fun onViewAllSkillsClicked() {
        // Handle view all skills logic
    }
}
