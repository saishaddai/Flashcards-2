package com.saishaddai.flashcards.repository

import com.saishaddai.flashcards.model.Deck
import com.saishaddai.flashcards.utils.SessionResult

interface StudyRepository {
    suspend fun completeSession(
        deck: Deck,
        cardsReviewed: Int,
        startTime: Long,
        endTime: Long,
        durationMillis: Long
    ): SessionResult
    
    suspend fun getCurrentStreak(): Int
}
