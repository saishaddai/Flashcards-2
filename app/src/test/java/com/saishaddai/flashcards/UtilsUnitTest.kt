package com.saishaddai.flashcards

import com.saishaddai.flashcards.data.Flashcard
import com.saishaddai.flashcards.utils.getRandomList
import org.junit.Test

import org.junit.Assert.*

class UtilsUnitTest {
    @Test
    fun getRandomList_empty_list_returns_empty_list() {
        val list = emptyList<Flashcard>()
        val result = getRandomList(list = list)
        assertEquals(emptyList<Flashcard>(), result)
    }

    @Test
    fun getRandomList_returns_scrambled_narrowed_list() {
        val list = listOf(Flashcard("question1", "answer1"),
            Flashcard("question2", "answer2"),
            Flashcard("question3", "answer3"),
            Flashcard("question4", "answer4"),
            Flashcard("question5", "answer5")
        )
        val result = getRandomList(size = 3, list = list)
        print(result)
        assertNotEquals(emptyList<Flashcard>(), result)
        assertNotEquals(list, result)
    }
}