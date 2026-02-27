package com.saishaddai.flashcards.repository

import com.saishaddai.flashcards.model.Flashcard
import com.saishaddai.flashcards.model.flashcards
import com.saishaddai.flashcards.utils.random

class FlashcardRepository {

    fun getFlashcards(deckId: Int, size: Int = 20): List<Flashcard> {
        return flashcards.filter { it.deckId == deckId }.random(size)
    }

}


