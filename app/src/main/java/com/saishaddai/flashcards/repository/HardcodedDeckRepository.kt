package com.saishaddai.flashcards.repository

import com.saishaddai.flashcards.model.Deck
import com.saishaddai.flashcards.model.decks
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class HardcodedDeckRepository : DeckRepository<Deck> {

    override suspend fun getData(): List<Deck> = withContext(Dispatchers.IO) {
        decks
    }

}