package com.saishaddai.flashcards.repository

import com.saishaddai.flashcards.repository.impl.JSONFlashcardRepository
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
        val deckId = 1
        val size = 5
        val result = repository.getData(deckId, size)

        assertTrue(result.all { it.deckId == deckId })
        assertTrue(result.size <= size)
    }

    @Test
    fun `getData returns empty list for non-existent deck id`() = runTest {
        val deckId = -1
        val result = repository.getData(deckId)

        assertTrue(result.isEmpty())
    }

    @Test
    fun `getData returns no more than requested size`() = runTest {
        val deckId = 1
        val size = 2
        val result = repository.getData(deckId, size)

        assertTrue(result.size <= size)
    }
}
