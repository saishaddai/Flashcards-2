package com.saishaddai.flashcards.model

import androidx.room.Entity
import kotlinx.serialization.Serializable

@Serializable
@Entity(tableName = "flashcards", primaryKeys = ["deckId", "id"])
data class Flashcard(
    val deckId: Int,
    val id: Int,
    val question: String,
    val answer: String
)
