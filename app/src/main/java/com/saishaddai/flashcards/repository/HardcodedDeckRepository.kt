package com.saishaddai.flashcards.repository

import com.saishaddai.flashcards.model.Deck
import com.saishaddai.flashcards.model.decks

class HardcodedDeckRepository : DeckRepository<Deck> {

    override fun getData(): List<Deck> = decks

}