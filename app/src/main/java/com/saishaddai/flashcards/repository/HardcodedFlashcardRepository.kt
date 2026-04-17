package com.saishaddai.flashcards.repository

import com.saishaddai.flashcards.model.Flashcard
import com.saishaddai.flashcards.model.getFlashcardsForDeck
import com.saishaddai.flashcards.utils.random
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class HardcodedFlashcardRepository : FlashcardRepository<Flashcard>{

    override suspend fun getData(id: Int, size: Int): List<Flashcard> = withContext(Dispatchers.IO) {
        getFlashcardsForDeck(id).random(size)
    }
}