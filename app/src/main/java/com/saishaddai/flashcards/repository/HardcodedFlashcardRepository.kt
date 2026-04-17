package com.saishaddai.flashcards.repository

import android.content.Context
import com.saishaddai.flashcards.model.Flashcard
import com.saishaddai.flashcards.model.getFlashcardsForDeck
import com.saishaddai.flashcards.utils.random
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class HardcodedFlashcardRepository(private val context: Context? = null): FlashcardRepository<Flashcard>{

    override suspend fun getData(id: Int, size: Int): List<Flashcard> = withContext(Dispatchers.IO) {
        getFlashcardsForDeck(id, context).random(size)
    }

}