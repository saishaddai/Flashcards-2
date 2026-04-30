package com.saishaddai.flashcards.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.saishaddai.flashcards.model.DeckType
import com.saishaddai.flashcards.model.Flashcard
import com.saishaddai.flashcards.repository.FlashcardRepository
import com.saishaddai.flashcards.repository.impl.JSONFlashcardRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class FlashcardViewModel(
    application: Application,
    private val deckId: Int,
    private val repository: FlashcardRepository<DeckType, Flashcard> = JSONFlashcardRepository(context = application)
) : AndroidViewModel(application) {
    private val _flashcards = MutableStateFlow<List<Flashcard>>(emptyList())
    val flashcards: StateFlow<List<Flashcard>> = _flashcards.asStateFlow()

    private val _showAnswer = MutableStateFlow(false)
    val showAnswer: StateFlow<Boolean> = _showAnswer.asStateFlow()

    private val _isFinished = MutableStateFlow(false)
    val isFinished: StateFlow<Boolean> = _isFinished.asStateFlow()

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    init {
        loadFlashcards()
    }

    private fun loadFlashcards() {
        viewModelScope.launch {
            _isLoading.value = true
            _flashcards.value = repository.getData(DeckType.fromId(deckId))
            _isLoading.value = false
        }
    }

    fun onShowResponseClicked() {
        _showAnswer.value = true
    }

    fun onPageChanged() {
        _showAnswer.value = false
    }

    fun onFinishSession() {
        _isFinished.value = true
    }
}
