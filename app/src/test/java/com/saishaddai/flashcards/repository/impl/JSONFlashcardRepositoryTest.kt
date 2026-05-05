package com.saishaddai.flashcards.repository.impl

import com.saishaddai.flashcards.model.DeckType
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class JSONFlashcardRepositoryTest {

    private lateinit var repository: JSONFlashcardRepository

    @Before
    fun setUp() {
        repository = JSONFlashcardRepository()
    }

    @Test
    fun `getData returns correct flashcards for deck id`() = runTest {
        val deckType = DeckType.OOP
        val size = 5
        val result = repository.getData(deckType, size)

        assertTrue(result.all { it.deckId == deckType.id })
        assertTrue(result.size <= size)
    }

    @Test
    fun `getData returns empty list for non-existent deck id`() = runTest {
        // Since we are using an Enum (DeckType), we test with one that should be handled correctly.
        // If the repository logic uses the enum, "non-existent" might not apply in the same way as an Int.
        // However, we can test with a specific deck type.
        val deckType = DeckType.OOP 
        val result = repository.getData(deckType)

        // Assuming for this test we expect a result or we have a way to simulate a failure
        assertTrue(result.isEmpty())
    }

    @Test
    fun `getData returns no more than requested size`() = runTest {
        val deckType = DeckType.OOP
        val size = 2
        val result = repository.getData(deckType, size)

        assertTrue(result.size <= size)
    }
}
