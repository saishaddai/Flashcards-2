package com.saishaddai.flashcards.utils

import com.saishaddai.flashcards.model.MasteryLevel
import org.junit.Assert.assertEquals
import org.junit.Test

class SessionCalculatorTest {

    private val calculator = SessionCalculator()

    @Test
    fun `calculateProgress returns correct base XP without streak or bonus`() {
        // Given: 20 cards viewed out of 20 total, 0 day streak, 0 accumulated progress
        val flashcardsViewed = 20
        val totalTopicFlashcards = 20
        val daysStreak = 0
        val accumulatedProgress = 0.0

        // When
        val result = calculator.calculateProgress(
            flashcardsViewed,
            totalTopicFlashcards,
            daysStreak,
            accumulatedProgress
        )

        // Then: Base is 100. Multiplier is 1.0. Bonus is 0. Total XP is 100.
        assertEquals(100.0, result.sessionProgress, 0.01)
        assertEquals(100.0, result.newProgress, 0.01)
        assertEquals(MasteryLevel.MASTERED, result.masteryLevel)
        assertEquals(0, result.streak)
        assertEquals(0, result.weeklyTimeMins)
    }

    @Test
    fun `calculateProgress applies streak multiplier`() {
        // Given: 10 cards viewed out of 20 total (base 50), 10 day streak, 0 accumulated
        val flashcardsViewed = 10
        val totalTopicFlashcards = 20
        val daysStreak = 10
        val accumulatedProgress = 0.0

        // When
        val result = calculator.calculateProgress(
            flashcardsViewed,
            totalTopicFlashcards,
            daysStreak,
            accumulatedProgress
        )

        // Then: Base is 50. Multiplier is 1 + (0.03 * 10) = 1.3. Total XP is 65.
        assertEquals(65.0, result.sessionProgress, 0.01)
        assertEquals(65.0, result.newProgress, 0.01)
        assertEquals(MasteryLevel.EXPERIENCED, result.masteryLevel)
        assertEquals(0, result.streak)
    }

    @Test
    fun `calculateProgress applies monthly bonus`() {
        // Given: 10 cards out of 20 (base 50), 30 day streak, 0 accumulated
        val flashcardsViewed = 10
        val totalTopicFlashcards = 20
        val daysStreak = 30
        val accumulatedProgress = 0.0

        // When
        val result = calculator.calculateProgress(
            flashcardsViewed,
            totalTopicFlashcards,
            daysStreak,
            accumulatedProgress
        )

        // Then: Base is 50. Multiplier is 1 + (0.03 * 30) = 1.9. Base * Multiplier = 95. Bonus = 10. Total = 105.
        // newProgress is capped at 100.
        assertEquals(105.0, result.sessionProgress, 0.01)
        assertEquals(100.0, result.newProgress, 0.01)
        assertEquals(MasteryLevel.MASTERED, result.masteryLevel)
    }

    @Test
    fun `calculateProgress correctly accumulates progress`() {
        // Given: base 10 XP, 0 streak, 50 accumulated progress
        val result = calculator.calculateProgress(
            flashcardsViewed = 2,
            totalTopicFlashcards = 20,
            daysStreak = 0,
            accumulatedProgress = 50.0
        )

        assertEquals(10.0, result.sessionProgress, 0.01)
        assertEquals(60.0, result.newProgress, 0.01)
    }
}
