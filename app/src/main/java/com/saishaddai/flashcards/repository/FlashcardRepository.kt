package com.saishaddai.flashcards.repository

interface FlashcardRepository<A> {

    suspend fun getData(id: Int, size: Int = 20): List<A>

}
