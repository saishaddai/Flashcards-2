package com.saishaddai.flashcards.repository

import com.saishaddai.flashcards.model.SessionSummary

interface SessionRepository {

    fun getAllSessions(): List<SessionSummary>
}