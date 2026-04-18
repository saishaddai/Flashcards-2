package com.saishaddai.flashcards.model

import kotlinx.serialization.Serializable

@Serializable
data class Flashcard(
    val deckId: Int,
    val id: Int,
    val question: String,
    val answer: String
)
