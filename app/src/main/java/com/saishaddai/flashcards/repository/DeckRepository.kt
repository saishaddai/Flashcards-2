package com.saishaddai.flashcards.repository

import com.saishaddai.flashcards.model.Deck
import com.saishaddai.flashcards.model.decks

class DeckRepository {

    fun getDecks() : List<Deck> = decks
}