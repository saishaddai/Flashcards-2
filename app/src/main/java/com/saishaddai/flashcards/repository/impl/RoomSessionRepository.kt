package com.saishaddai.flashcards.repository.impl

import com.saishaddai.flashcards.data.local.SessionSummaryDao
import com.saishaddai.flashcards.model.SessionSummary
import com.saishaddai.flashcards.repository.SessionRepository
import kotlinx.coroutines.flow.Flow

class RoomSessionRepository(
    private val sessionSummaryDao: SessionSummaryDao
) : SessionRepository {
    override fun getAllSessions(): Flow<List<SessionSummary>> = sessionSummaryDao.getAllSessions()

    override suspend fun getSessionByDeckId(deckId: Int): SessionSummary? = sessionSummaryDao.getSessionByDeckId(deckId)

    override suspend fun insertSession(session: SessionSummary) {
        sessionSummaryDao.insertSession(session)
    }

    override suspend fun updateSession(session: SessionSummary) {
        sessionSummaryDao.updateSession(session)
    }

    override suspend fun deleteSession(session: SessionSummary) {
        sessionSummaryDao.deleteSession(session)
    }
}
