package com.saishaddai.flashcards.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.saishaddai.flashcards.model.DeckType
import com.saishaddai.flashcards.model.Flashcard
import com.saishaddai.flashcards.repository.FlashcardRepository
import com.saishaddai.flashcards.repository.SettingsRepository
import com.saishaddai.flashcards.repository.impl.JSONFlashcardRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class FlashcardViewModel(
    application: Application,
    private val deckId: Int,
    private val repository: FlashcardRepository<DeckType, Flashcard> = JSONFlashcardRepository(context = application),
    private val settingsRepository: SettingsRepository
) : AndroidViewModel(application) {
    private val _flashcards = MutableStateFlow<List<Flashcard>>(emptyList())
    val flashcards: StateFlow<List<Flashcard>> = _flashcards.asStateFlow()

    private val _showAnswer = MutableStateFlow(false)
    private val _flashcardsPerSession = MutableStateFlow(20)
    val showAnswer: StateFlow<Boolean> = _showAnswer.asStateFlow()
    val flashcardsPerSession: StateFlow<Int> = _flashcardsPerSession.asStateFlow()

    private val _isFinished = MutableStateFlow(false)
    val isFinished: StateFlow<Boolean> = _isFinished.asStateFlow()

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    init {
        observeSettings()
        loadFlashcards()
    }

    private fun observeSettings() {
        viewModelScope.launch {
            settingsRepository.getSettings().collectLatest { settings ->
                _showAnswer.value = settings.showAnswers
                _flashcardsPerSession.value = settings.flashcardsPerSession
            }
        }
    }

    private fun loadFlashcards() {
        viewModelScope.launch {
            _isLoading.value = true
            val settings = settingsRepository.getSettings().first()
            Log.d("FlashcardViewModel", "Loaded settings: flashcardsPerSession=${settings.flashcardsPerSession}")
            _showAnswer.value = settings.showAnswers
            _flashcardsPerSession.value = settings.flashcardsPerSession
            _flashcards.value = repository.getData(
                type = DeckType.fromId(deckId),
                size = settings.flashcardsPerSession
            )
            _isLoading.value = false
        }
    }

    fun onShowResponseClicked() {
        _showAnswer.value = true
    }

    fun onPageChanged() {
        viewModelScope.launch {
            val settings = settingsRepository.getSettings().first()
            _showAnswer.value = settings.showAnswers
        }
    }

    fun onFinishSession() {
        _isFinished.value = true
    }
}
