package com.saishaddai.flashcards.repository.impl

import com.saishaddai.flashcards.model.DeckType
import com.saishaddai.flashcards.model.Flashcard
import com.saishaddai.flashcards.model.sessions
import com.saishaddai.flashcards.repository.FlashcardRepository
import com.saishaddai.flashcards.repository.SessionRepository
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock

class JSONDeckRepositoryTest {

    private lateinit var repository: JSONDeckRepository
    private val flashcardRepository: FlashcardRepository<DeckType, Flashcard> = mock {
        onBlocking { getDataCount(any()) } doReturn 0
    }
    private val sessionRepository: SessionRepository = mock {
        onBlocking { getAllSessions() } doReturn sessions
    }

    @Before
    fun setUp() {
        repository = JSONDeckRepository(flashcardRepository, sessionRepository)
    }

    @Test
    fun `getData returns all decks from the list`() = runTest {
        val result = repository.getData()
        assertEquals(20, result.size)
    }

    @Test
    fun `getData applies mastery from sessions`() = runTest {
        // sessions in model has: SessionSummary(1, 89), SessionSummary(2, 20), SessionSummary(3, 54), SessionSummary(4, 0)
        val result = repository.getData()

        assertEquals(89, result.find { it.id == 1 }?.mastery)
        assertEquals(20, result.find { it.id == 2 }?.mastery)
        assertEquals(54, result.find { it.id == 3 }?.mastery)
        assertEquals(0, result.find { it.id == 4 }?.mastery)
        assertEquals(0, result.find { it.id == 5 }?.mastery) // Not in sessions
    }

    @Test
    fun `first deck is selected by default`() = runTest {
        val result = repository.getData()
        assertTrue(result.first().isSelected)
    }
}
