package com.saishaddai.flashcards.repository

import com.saishaddai.flashcards.model.decks
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class HardcodedDeckRepositoryTest {

    private lateinit var repository: HardcodedDeckRepository

    @Before
    fun setUp() {
        repository = HardcodedDeckRepository()
    }

    @Test
    fun `getData returns correct decks`() = runTest {
        val result = repository.getData()
        assertTrue(result.isNotEmpty())
        assertTrue(result.size == decks.size)
    }
}