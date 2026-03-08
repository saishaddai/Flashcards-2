package com.saishaddai.flashcards.repository

interface DeckRepository<A> {

    suspend fun getData(): List<A>

}