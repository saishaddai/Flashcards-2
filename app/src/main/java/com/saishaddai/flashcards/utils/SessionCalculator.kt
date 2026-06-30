package com.saishaddai.flashcards.utils

import com.saishaddai.flashcards.model.MasteryLevel
import kotlin.math.min

class SessionCalculator {
    fun calculateProgress(
        flashcardsViewed: Int,
        totalTopicFlashcards: Int,
        daysStreak: Int,
        accumulatedProgress: Double
    ): SessionResult {
        // 1. Base progress
        val base = (100.0 / totalTopicFlashcards) * flashcardsViewed

        // 2. Consistency multiplier
        val multiplier = 1 + (0.03 * daysStreak)

        // 3. Monthly Bonus
        var bonus = 0.0
        if (daysStreak >= 30) {
            bonus = 10.0
        }

        // 4. Session progress
        val sessionProgress = (base * multiplier) + bonus

        // 5. New accumulated progress (max 100)
        val newProgress = min(accumulatedProgress + sessionProgress, 100.0)

        // 6. Mastery Level
        val masteryLevel = MasteryLevel.fromProgress(newProgress.toInt())

        return SessionResult(sessionProgress, newProgress, masteryLevel)
    }
}

data class SessionResult(
    val sessionProgress: Double,
    val newProgress: Double,
    val masteryLevel: MasteryLevel
)
