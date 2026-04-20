package com.saishaddai.flashcards.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.saishaddai.flashcards.model.Deck
import com.saishaddai.flashcards.repository.DeckRepository
import com.saishaddai.flashcards.repository.impl.JSONDeckRepository
import com.saishaddai.flashcards.repository.impl.JSONFlashcardRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch


class DecksViewModel(
    application: Application,
    private val repository: DeckRepository<Deck> = JSONDeckRepository(
        JSONFlashcardRepository(application)
    )
) : AndroidViewModel(application) {
    private val _decks = MutableStateFlow<List<Deck>>(emptyList())
    val decks: StateFlow<List<Deck>> = _decks.asStateFlow()

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    init {
        loadDecks()
    }

    private fun loadDecks() {
        viewModelScope.launch {
            _isLoading.value = true
            _decks.value = repository.getData()
            _isLoading.value = false
        }
    }

    fun onDeckSelected(selectedDeck: Deck) {
        _decks.value = _decks.value.map {
            it.copy(isSelected = it.id == selectedDeck.id)
        }
    }

    fun getRandomDeck(): Deck? {
        return _decks.value.randomOrNull()
    }
}
