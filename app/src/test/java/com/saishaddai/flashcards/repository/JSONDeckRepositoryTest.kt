package com.saishaddai.flashcards.repository

import com.saishaddai.flashcards.model.decks
import com.saishaddai.flashcards.repository.impl.JSONDeckRepository
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class JSONDeckRepositoryTest {

    private lateinit var repository: JSONDeckRepository

    @Before
    fun setUp() {
        repository = JSONDeckRepository()
    }

    @Test
    fun `getData returns correct decks`() = runTest {
        val result = repository.getData()
        assertTrue(result.isNotEmpty())
        assertTrue(result.size == decks.size)
    }
}