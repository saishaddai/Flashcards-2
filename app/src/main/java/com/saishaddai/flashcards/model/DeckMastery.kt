package com.saishaddai.flashcards.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "deck_mastery")
data class DeckMastery(
    @PrimaryKey val deckId: Int,
    val deckName: String,
    val progress: Double = 0.0,
    val level: String = "Novato",
    val lastReviewed: Long = System.currentTimeMillis()
)
