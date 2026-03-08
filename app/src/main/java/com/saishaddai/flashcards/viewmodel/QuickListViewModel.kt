package com.saishaddai.flashcards.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.saishaddai.flashcards.model.Flashcard
import com.saishaddai.flashcards.repository.FlashcardRepository
import com.saishaddai.flashcards.repository.HardcodedFlashcardRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class QuickListViewModel(
    private val deckId: Int,
    private val repository: FlashcardRepository<Flashcard> = HardcodedFlashcardRepository()
) : ViewModel() {
    private val _flashcards = MutableStateFlow<List<Flashcard>>(emptyList())
    val flashcards: StateFlow<List<Flashcard>> = _flashcards.asStateFlow()

    init {
        loadFlashcards()
    }

    private fun loadFlashcards() {
        viewModelScope.launch {
            _flashcards.value = repository.getData(deckId)
        }
    }
}
