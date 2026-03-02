package com.saishaddai.flashcards.repository

import com.saishaddai.flashcards.model.Flashcard
import com.saishaddai.flashcards.model.flashcards
import com.saishaddai.flashcards.utils.random

interface FlashcardRepository<A> {

    fun getData(id: Int, size: Int = 20): List<A>

}


