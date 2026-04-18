package com.saishaddai.flashcards.repository.impl

import com.saishaddai.flashcards.model.Flashcard
import com.saishaddai.flashcards.repository.FlashcardRepository
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

class JSONDeckRepositoryTest {

    private lateinit var repository: JSONDeckRepository
    private val flashcardRepository: FlashcardRepository<Flashcard> = mock()

    @Before
    fun setUp() {
        repository = JSONDeckRepository(flashcardRepository)
    }

    @Test
    fun `getData returns all decks from the list`() = runTest {
        val result = repository.getData()
        assertEquals(19, result.size)
    }

    @Test
    fun `getData updates cardCount using flashcardRepository`() = runTest {
        val deckId = 1
        whenever(flashcardRepository.getDataCount(deckId)).thenReturn(10)
        
        val result = repository.getData()
        val deck = result.find { it.id == deckId }
        
        assertEquals(10, deck?.cardCount)
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
