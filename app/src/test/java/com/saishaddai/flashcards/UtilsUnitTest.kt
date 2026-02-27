package com.saishaddai.flashcards

import com.saishaddai.flashcards.model.Flashcard
import com.saishaddai.flashcards.utils.random
import org.junit.Test

import org.junit.Assert.*

class UtilsUnitTest {
    val flashcards = listOf(
        Flashcard(1, 1, "1: What is the capital of France?", "Paris", false),
        Flashcard(1, 2, "1: What is the largest planet in our solar system?", "Jupiter", false),
        Flashcard(1, 3, "1: What is the smallest country in the world?", "Vatican City", false),
        Flashcard(1, 4, "1: What is the highest mountain in the world?", "Mount Everest", false),
        Flashcard(2, 5, "2: What is the capital of France?", "Paris", false),
    )

    @Test
    fun random_empty_list_returns_empty_list() {
        val list = emptyList<Flashcard>()
        val result = list.random(20)
        assertEquals(emptyList<Flashcard>(), result)
    }

    @Test
    fun random_limited_size_returns_scrambled_narrowed_list() {
        val result = flashcards.random(size = 3)
        print(result)
        assertNotEquals(emptyList<Flashcard>(), result)
        assertNotEquals(flashcards, result)
        assertEquals(3, result.size)
    }

    @Test
    fun random_over_size_returns_scrambled_narrowed_list() {
        val result = flashcards.random(size = 50)
        print(result)
        assertNotEquals(emptyList<Flashcard>(), result)
        assertNotEquals(flashcards, result)
        assertEquals(5, result.size)
    }
}