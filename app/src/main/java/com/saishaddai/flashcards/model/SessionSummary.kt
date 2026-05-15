package com.saishaddai.flashcards.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "session_summaries")
data class SessionSummary(
    @PrimaryKey val deckId: Int,
    val currentXP: Int
)

val sessions = listOf(
    SessionSummary(1, 89),
    SessionSummary(2, 20),
    SessionSummary(3, 54),
    SessionSummary(4, 0)
)
