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
import java.time.LocalDate

class RoomStudyRepository(
    private val studyDao: StudyDao,
    private val settingsRepository: SettingsRepository,
    private val calculator: SessionCalculator = SessionCalculator()
) : StudyRepository {

    override suspend fun completeSession(
        deck: Deck,
        cardsReviewed: Int,
        startTime: Long,
        endTime: Long
    ): SessionResult {
        val today = LocalDate.now().toString()
        val streak = calculateStreak()
        val currentMastery = studyDao.getDeckMastery(deck.id)
        val progresoAcumulado = currentMastery?.progress ?: 0.0

        // Calculate results using the formula
        val result = calculator.calculateProgress(
            flashcardsVistas = cardsReviewed,
            totalFlashcardsTema = if (deck.cardCount > 0) deck.cardCount else 20, // Fallback if count is 0
            rachaDias = streak,
            progresoAcumulado = progresoAcumulado
        )

        // Update Room
        val session = StudySession(
            deckId = deck.id,
            cardsReviewed = cardsReviewed,
            startTime = startTime,
            endTime = endTime,
            xpGained = result.avanceSesion,
            streakAtTime = streak
        )

        val newDeckMastery = DeckMastery(
            deckId = deck.id,
            deckName = deck.name,
            progress = result.nuevoProgreso,
            level = result.titulo,
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
        var currentDate = LocalDate.now()
        
        // Check today
        if (activityMap[currentDate.toString()]?.isGoalMet == true) {
            streak++
            currentDate = currentDate.minusDays(1)
            while (activityMap[currentDate.toString()]?.isGoalMet == true) {
                streak++
                currentDate = currentDate.minusDays(1)
            }
        } else {
            // Check from yesterday
            currentDate = currentDate.minusDays(1)
            while (activityMap[currentDate.toString()]?.isGoalMet == true) {
                streak++
                currentDate = currentDate.minusDays(1)
            }
        }
        return streak
    }
}
