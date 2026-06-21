package com.saishaddai.flashcards.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.saishaddai.flashcards.model.Deck
import com.saishaddai.flashcards.repository.DeckRepository
import com.saishaddai.flashcards.utils.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class DecksUiState(
    val decks: List<Deck> = emptyList(),
    val showEmptyDeckDialog: Boolean = false
)

class DecksViewModel(
    application: Application,
    private val repository: DeckRepository<Deck>
) : AndroidViewModel(application) {
    private val _uiState = MutableStateFlow<UiState<DecksUiState>>(UiState.Loading)
    val uiState: StateFlow<UiState<DecksUiState>> = _uiState.asStateFlow()

    init {
        loadDecks()
    }

    fun loadDecks() {
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            try {
                val loadedDecks = repository.getData()
                val finalDecks = if (loadedDecks.isNotEmpty()) {
                    if (loadedDecks.none { it.isSelected }) {
                        loadedDecks.mapIndexed { index, deck ->
                            deck.copy(isSelected = index == 0)
                        }
                    } else {
                        loadedDecks
                    }
                } else {
                    emptyList()
                }
                _uiState.value = UiState.Success(DecksUiState(decks = finalDecks))
            } catch (e: Exception) {
                _uiState.value = UiState.Error("Failed to load decks", e)
            }
        }
    }

    fun onDeckSelected(selectedDeck: Deck) {
        val currentState = _uiState.value
        if (currentState is UiState.Success) {
            val updatedDecks = currentState.data.decks.map {
                it.copy(isSelected = it.id == selectedDeck.id)
            }
            _uiState.value = UiState.Success(currentState.data.copy(decks = updatedDecks))
        }
    }

    fun getRandomDeck(): Deck? {
        return (_uiState.value as? UiState.Success)?.data?.decks?.randomOrNull()
    }

    fun onStartSession(deck: Deck? = null) {
        val currentState = _uiState.value
        if (currentState is UiState.Success) {
            val targetDeck = deck ?: currentState.data.decks.find { it.isSelected }
            if (targetDeck == null || targetDeck.cardCount == 0) {
                _uiState.value = UiState.Success(currentState.data.copy(showEmptyDeckDialog = true))
            }
        }
    }

    fun dismissEmptyDeckDialog() {
        val currentState = _uiState.value
        if (currentState is UiState.Success) {
            _uiState.value = UiState.Success(currentState.data.copy(showEmptyDeckDialog = false))
        }
    }
}
