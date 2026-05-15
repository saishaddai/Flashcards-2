package com.saishaddai.flashcards.repository.impl

import com.saishaddai.flashcards.model.SessionSummary
import com.saishaddai.flashcards.repository.SessionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class HardcodedSessionRepository : SessionRepository {
    override fun getAllSessions(): Flow<List<SessionSummary>> = flowOf(emptyList())

    override suspend fun getSessionByDeckId(deckId: Int): SessionSummary? = null

    override suspend fun insertSession(session: SessionSummary) {}

    override suspend fun updateSession(session: SessionSummary) {}

    override suspend fun deleteSession(session: SessionSummary) {}
}
