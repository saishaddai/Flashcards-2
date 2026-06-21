package com.saishaddai.flashcards.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.saishaddai.flashcards.model.Deck
import com.saishaddai.flashcards.repository.StudyRepository
import com.saishaddai.flashcards.utils.SessionResult
import com.saishaddai.flashcards.utils.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class FinishSessionUiData(
    val sessionResult: SessionResult? = null,
    val navigateToDeckList: Boolean = false
)

class FinishSessionViewModel(
    private val studyRepository: StudyRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow<UiState<FinishSessionUiData>>(UiState.Loading)
    val uiState: StateFlow<UiState<FinishSessionUiData>> = _uiState.asStateFlow()

    fun saveSession(deck: Deck, cardsReviewed: Int, startTime: Long, endTime: Long) {
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            try {
                val result = studyRepository.completeSession(deck, cardsReviewed, startTime, endTime)
                _uiState.value = UiState.Success(FinishSessionUiData(sessionResult = result))
            } catch (e: Exception) {
                _uiState.value = UiState.Error("Failed to save session", e)
            }
        }
    }

    fun onBackToDecksClicked() {
        val currentState = _uiState.value
        if (currentState is UiState.Success) {
            _uiState.value = UiState.Success(currentState.data.copy(navigateToDeckList = true))
        }
    }

    fun onNavigationHandled() {
        val currentState = _uiState.value
        if (currentState is UiState.Success) {
            _uiState.value = UiState.Success(currentState.data.copy(navigateToDeckList = false))
        }
    }
}
