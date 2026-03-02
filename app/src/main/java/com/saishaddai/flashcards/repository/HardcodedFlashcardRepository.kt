package com.saishaddai.flashcards.repository

import com.saishaddai.flashcards.model.Flashcard
import com.saishaddai.flashcards.model.flashcards
import com.saishaddai.flashcards.utils.random

class HardcodedFlashcardRepository : FlashcardRepository<Flashcard>{

    override fun getData(id: Int, size: Int): List<Flashcard> {
        return flashcards.filter { it.deckId == id }.random(size)
    }
}