package com.saishaddai.flashcards.repository

import com.saishaddai.flashcards.model.Flashcard
import com.saishaddai.flashcards.model.flashcards

class FlashcardRepository {

    fun getFlashcards(deckId: Int): List<Flashcard> {
        return flashcards.filter { it.deckId == deckId }
    }

    fun getFlashcard(deckId: Int, flashcardId: Int): Flashcard? {
        return flashcards.firstOrNull { it.deckId == deckId && it.id == flashcardId }
    }
}


