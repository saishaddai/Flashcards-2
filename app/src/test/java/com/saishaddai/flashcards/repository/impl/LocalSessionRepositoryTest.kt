package com.saishaddai.flashcards.repository.impl

import com.saishaddai.flashcards.data.local.SessionSummaryDao
import com.saishaddai.flashcards.model.SessionSummary
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

class LocalSessionRepositoryTest {

    private lateinit var repository: LocalSessionRepository
    private val sessionSummaryDao: SessionSummaryDao = mock()

    @Before
    fun setUp() {
        repository = LocalSessionRepository(sessionSummaryDao)
    }

    @Test
    fun `getAllSessions returns data from DAO`() = runTest {
        // Given
        val sessions = listOf(SessionSummary(1, 100))
        whenever(sessionSummaryDao.getAllSessions()).thenReturn(flowOf(sessions))

        // When
        val result = repository.getAllSessions().first()

        // Then
        assertEquals(sessions, result)
    }

    @Test
    fun `insertSession calls DAO`() = runTest {
        val session = SessionSummary(1, 100)
        repository.insertSession(session)
        verify(sessionSummaryDao).insertSession(session)
    }
}
