package com.saishaddai.flashcards.repository

import android.content.Context
import com.saishaddai.flashcards.model.Deck
import com.saishaddai.flashcards.model.decks
import com.saishaddai.flashcards.model.getFlashcardsForDeck
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class HardcodedDeckRepository(private val context: Context? = null) : DeckRepository<Deck> {

    override suspend fun getData(): List<Deck> = withContext(Dispatchers.IO) {
        decks.onEach {
            if (it.cardCount == 0) {
                it.cardCount = getFlashcardsForDeck(it.id, context).size
            }
        }
    }

}