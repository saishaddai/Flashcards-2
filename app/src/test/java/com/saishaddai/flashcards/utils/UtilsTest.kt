package com.saishaddai.flashcards.utils

import com.saishaddai.flashcards.model.Flashcard
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class UtilsTest {

    private lateinit var flashcards: List<Flashcard>

    @Before
    fun setUp() {
        flashcards = listOf(
            Flashcard(1, 1, "Q1", "A1"),
            Flashcard(1, 2, "Q2", "A2"),
            Flashcard(1, 3, "Q3", "A3"),
            Flashcard(1, 4, "Q4", "A4"),
            Flashcard(2, 5, "Q5", "A5")
        )
    }

    @Test
    fun `random with empty list returns empty list`() = runTest {
        val list = emptyList<Flashcard>()
        val result = list.random(20)
        Assert.assertTrue(result.isEmpty())
    }

    @Test
    fun `random limited size returns scrambled narrowed list`() = runTest {
        val result = flashcards.random(size = 3)
        Assert.assertFalse(result.isEmpty())
        Assert.assertNotEquals(flashcards, result)
        Assert.assertEquals(3, result.size)
    }

    @Test
    fun `random over size returns original list with scrambled elements`() = runTest {
        val result = flashcards.random(size = 50)
        Assert.assertFalse(result.isEmpty())
        Assert.assertEquals(flashcards.size, result.size)
        Assert.assertNotEquals(flashcards, result)
    }
}