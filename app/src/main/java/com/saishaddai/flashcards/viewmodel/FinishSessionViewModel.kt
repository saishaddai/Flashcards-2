package com.saishaddai.flashcards.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.saishaddai.flashcards.model.Deck
import com.saishaddai.flashcards.repository.StudyRepository
import com.saishaddai.flashcards.utils.SessionResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class FinishSessionViewModel(
    private val studyRepository: StudyRepository
) : ViewModel() {
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _navigateToDeckList = MutableStateFlow(false)
    val navigateToDeckList: StateFlow<Boolean> = _navigateToDeckList.asStateFlow()

    private val _sessionResult = MutableStateFlow<SessionResult?>(null)
    val sessionResult: StateFlow<SessionResult?> = _sessionResult.asStateFlow()

    fun saveSession(deck: Deck, cardsReviewed: Int, startTime: Long, endTime: Long) {
        viewModelScope.launch {
            _isLoading.value = true
            val result = studyRepository.completeSession(deck, cardsReviewed, startTime, endTime)
            _sessionResult.value = result
            _isLoading.value = false
        }
    }

    fun onBackToDecksClicked() {
        _navigateToDeckList.value = true
    }

    fun onNavigationHandled() {
        _navigateToDeckList.value = false
    }
}
