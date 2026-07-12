package com.saishaddai.flashcards.repository.impl

import com.saishaddai.flashcards.data.local.StudyDao
import com.saishaddai.flashcards.model.DailyActivity
import com.saishaddai.flashcards.model.Deck
import com.saishaddai.flashcards.model.DeckMastery
import com.saishaddai.flashcards.model.StudySession
import com.saishaddai.flashcards.repository.SettingsRepository
import com.saishaddai.flashcards.repository.StudyRepository
import com.saishaddai.flashcards.utils.SessionCalculator
import com.saishaddai.flashcards.utils.SessionResult
import kotlinx.coroutines.flow.first
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class RoomStudyRepository(
    private val studyDao: StudyDao,
    private val settingsRepository: SettingsRepository,
    private val calculator: SessionCalculator = SessionCalculator()
) : StudyRepository {

    private val dateFormatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

    override suspend fun completeSession(
        deck: Deck,
        cardsReviewed: Int,
        startTime: Long,
        endTime: Long,
        durationMillis: Long
    ): SessionResult {
        val today = dateFormatter.format(Calendar.getInstance().time)
        val streak = calculateStreak()
        val currentMastery = studyDao.getDeckMastery(deck.id)
        val accumulatedProgress = currentMastery?.progress ?: 0.0

        // Calculate results using the formula
        val result = calculator.calculateProgress(
            flashcardsViewed = cardsReviewed,
            totalTopicFlashcards = if (deck.cardCount > 0) deck.cardCount else 20, // Fallback if count is 0
            daysStreak = streak,
            accumulatedProgress = accumulatedProgress
        )

        // Update Room
        val session = StudySession(
            deckId = deck.id,
            cardsReviewed = cardsReviewed,
            startTime = startTime,
            endTime = endTime,
            durationMillis = durationMillis,
            xpGained = result.sessionProgress,
            streakAtTime = streak
        )

        val newDeckMastery = DeckMastery(
            deckId = deck.id,
            deckName = deck.name,
            progress = result.newProgress,
            level = result.masteryLevel.name,
            lastReviewed = endTime
        )

        // Update Daily Activity
        val dailyGoal = settingsRepository.getSettings().first().dailyStudyGoal
        val currentDailyActivity = studyDao.getDailyActivity(today)
        val newTotalCards = (currentDailyActivity?.cardsReviewed ?: 0) + cardsReviewed
        val isGoalMet = newTotalCards >= dailyGoal
        
        val newDailyActivity = DailyActivity(
            date = today,
            cardsReviewed = newTotalCards,
            isGoalMet = isGoalMet
        )

        studyDao.completeSession(session, newDeckMastery, newDailyActivity)

        return result
    }

    override suspend fun getCurrentStreak(): Int = calculateStreak()

    private suspend fun calculateStreak(): Int {
        val activities = studyDao.getRecentActivity().first()
        val activityMap = activities.associateBy { it.date }
        
        var streak = 0
        val calendar = Calendar.getInstance()
        
        // Check today
        var currentDateStr = dateFormatter.format(calendar.time)
        if (activityMap[currentDateStr]?.isGoalMet == true) {
            streak++
            calendar.add(Calendar.DAY_OF_YEAR, -1)
            currentDateStr = dateFormatter.format(calendar.time)
            while (activityMap[currentDateStr]?.isGoalMet == true) {
                streak++
                calendar.add(Calendar.DAY_OF_YEAR, -1)
                currentDateStr = dateFormatter.format(calendar.time)
            }
        } else {
            // Check from yesterday
            calendar.add(Calendar.DAY_OF_YEAR, -1)
            currentDateStr = dateFormatter.format(calendar.time)
            while (activityMap[currentDateStr]?.isGoalMet == true) {
                streak++
                calendar.add(Calendar.DAY_OF_YEAR, -1)
                currentDateStr = dateFormatter.format(calendar.time)
            }
        }
        return streak
    }
}
