package com.saishaddai.flashcards.viewmodel

import androidx.lifecycle.ViewModel
import com.saishaddai.flashcards.model.Flashcard
import com.saishaddai.flashcards.repository.FlashcardRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class QuickListViewModel(
    private val deckId: Int,
    private val repository: FlashcardRepository = FlashcardRepository()
) : ViewModel() {
    private val _flashcards = MutableStateFlow<List<Flashcard>>(emptyList())
    val flashcards: StateFlow<List<Flashcard>> = _flashcards.asStateFlow()

    init {
        loadFlashcards()
    }

    private fun loadFlashcards() {
        _flashcards.value = repository.getFlashcards(deckId)
    }
}
