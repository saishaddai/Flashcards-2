package com.saishaddai.flashcards.repository

interface DeckRepository<A> {

    fun getData(): List<A>

}