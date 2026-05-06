package com.saishaddai.flashcards.repository.impl

import com.saishaddai.flashcards.model.SessionSummary
import com.saishaddai.flashcards.repository.SessionRepository

class HardcodedSessionRepository : SessionRepository {
    override fun getAllSessions(): List<SessionSummary> = emptyList()
}
