package com.saishaddai.flashcards.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.saishaddai.flashcards.model.DeckType
import com.saishaddai.flashcards.model.Flashcard
import com.saishaddai.flashcards.repository.FlashcardRepository
import com.saishaddai.flashcards.repository.SettingsRepository
import com.saishaddai.flashcards.utils.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

data class FlashcardsUiData(
    val flashcards: List<Flashcard> = emptyList(),
    val showAnswer: Boolean = false,
    val isFinished: Boolean = false
)

class FlashcardViewModel(
    application: Application,
    private val deckId: Int,
    private val repository: FlashcardRepository<DeckType, Flashcard>,
    private val settingsRepository: SettingsRepository
) : AndroidViewModel(application) {
    private val _uiState = MutableStateFlow<UiState<FlashcardsUiData>>(UiState.Loading)
    val uiState: StateFlow<UiState<FlashcardsUiData>> = _uiState.asStateFlow()

    private var startTime: Long = System.currentTimeMillis()
    private var cardsReviewedCount: Int = 0
    private var endTime: Long = 0
    private var totalActiveTimeMillis: Long = 0L
    private var lastStartTime: Long = 0L

    fun getSessionSummary() = Quadruple(cardsReviewedCount, startTime, endTime, totalActiveTimeMillis)

    fun startTimer() {
        if (lastStartTime == 0L) {
            lastStartTime = System.currentTimeMillis()
        }
    }

    fun pauseTimer() {
        if (lastStartTime != 0L) {
            totalActiveTimeMillis += System.currentTimeMillis() - lastStartTime
            lastStartTime = 0L
        }
    }

    init {
        loadFlashcards()
    }

    fun loadFlashcards() {
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            try {
                val settings = settingsRepository.getSettings().first()
                val flashcards = repository.getData(
                    type = DeckType.fromId(deckId),
                    size = settings.flashcardsPerSession
                )
                _uiState.value = UiState.Success(
                    FlashcardsUiData(
                        flashcards = flashcards,
                        showAnswer = settings.showAnswers
                    )
                )
                startTimer() // Start timer once loaded
            } catch (e: Exception) {
                _uiState.value = UiState.Error("Failed to load flashcards", e)
            }
        }
    }

    fun onShowResponseClicked() {
        val currentState = _uiState.value
        if (currentState is UiState.Success) {
            _uiState.value = UiState.Success(currentState.data.copy(showAnswer = true))
        }
    }

    fun onPageChanged(page: Int) {
        cardsReviewedCount = maxOf(cardsReviewedCount, page + 1)
        viewModelScope.launch {
            val settings = settingsRepository.getSettings().first()
            val currentState = _uiState.value
            if (currentState is UiState.Success) {
                _uiState.value = UiState.Success(currentState.data.copy(showAnswer = settings.showAnswers))
            }
        }
    }

    fun onFinishSession(finalPage: Int) {
        pauseTimer()
        cardsReviewedCount = maxOf(cardsReviewedCount, finalPage + 1)
        endTime = System.currentTimeMillis()
        val currentState = _uiState.value
        if (currentState is UiState.Success) {
            _uiState.value = UiState.Success(currentState.data.copy(isFinished = true))
        }
    }
}

data class Quadruple<out A, out B, out C, out D>(
    val first: A,
    val second: B,
    val third: C,
    val fourth: D
) : java.io.Serializable {
    override fun toString(): String = "($first, $second, $third, $fourth)"
}
