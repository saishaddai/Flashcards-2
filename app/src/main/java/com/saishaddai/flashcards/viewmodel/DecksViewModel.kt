package com.saishaddai.flashcards.viewmodel

import androidx.lifecycle.ViewModel
import com.saishaddai.flashcards.model.Deck
import com.saishaddai.flashcards.repository.DeckRepository
import com.saishaddai.flashcards.repository.HardcodedDeckRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class DecksViewModel(
    private val repository: DeckRepository<Deck> = HardcodedDeckRepository()
) : ViewModel() {
    private val _decks = MutableStateFlow<List<Deck>>(emptyList())
    val flashcards: StateFlow<List<Deck>> = _decks.asStateFlow()

    init {
        loadFlashcards()
    }

    private fun loadFlashcards() {
        _decks.value = repository.getData()
    }
}