package com.saishaddai.flashcards.repository

import com.saishaddai.flashcards.model.SessionSummary
import kotlinx.coroutines.flow.Flow

interface SessionRepository {
    fun getAllSessions(): Flow<List<SessionSummary>>
    suspend fun getSessionByDeckId(deckId: Int): SessionSummary?
    suspend fun insertSession(session: SessionSummary)
    suspend fun updateSession(session: SessionSummary)
    suspend fun deleteSession(session: SessionSummary)
}
