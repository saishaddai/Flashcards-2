package com.saishaddai.flashcards.data

data class Flashcard(val id: String,
                     val deckId: String,
                     val question: String,
                     val answer: String)