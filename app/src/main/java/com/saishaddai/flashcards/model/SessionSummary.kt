package com.saishaddai.flashcards.model

data class SessionSummary(val deckId: Int, val currentXP: Int)

val sessions = listOf(
    SessionSummary(1, 89),
    SessionSummary(2, 20),
    SessionSummary(3, 54),
    SessionSummary(4, 0)
)