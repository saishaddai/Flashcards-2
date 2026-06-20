package com.saishaddai.flashcards.utils

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

        // 3. Monthly Bonus & Title
        var bonus = 0.0
        val title = when {
            daysStreak >= 30 -> {
                bonus = 10.0
                "Master"
            }
            accumulatedProgress < 20 -> "Novice"
            accumulatedProgress < 50 -> "Intermediate"
            accumulatedProgress < 80 -> "Advanced"
            else -> "Expert"
        }

        // 4. Session progress
        val sessionProgress = (base * multiplier) + bonus

        // 5. New accumulated progress (max 100)
        val newProgress = min(accumulatedProgress + sessionProgress, 100.0)

        return SessionResult(sessionProgress, newProgress, title)
    }
}

data class SessionResult(
    val sessionProgress: Double,
    val newProgress: Double,
    val title: String
)
