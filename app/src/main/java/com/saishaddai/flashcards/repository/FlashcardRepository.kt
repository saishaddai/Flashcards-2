package com.saishaddai.flashcards.repository

interface FlashcardRepository<A, B> {

    suspend fun getData(type: A, size: Int = 20): List<B>

    suspend fun getDataCount(type: A): Int

}
