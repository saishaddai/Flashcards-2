package com.saishaddai.flashcards.repository.impl

import com.saishaddai.flashcards.data.local.StudyDao
import com.saishaddai.flashcards.model.DailyActivity
import com.saishaddai.flashcards.model.Deck
import com.saishaddai.flashcards.model.DeckMastery
import com.saishaddai.flashcards.model.MasteryLevel
import com.saishaddai.flashcards.repository.SettingsRepository
import com.saishaddai.flashcards.repository.UserSettings
import com.saishaddai.flashcards.utils.SessionCalculator
import com.saishaddai.flashcards.utils.SessionResult
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.eq
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class LocalStudyRepositoryTest {

    private lateinit var repository: LocalStudyRepository
    private val studyDao: StudyDao = mock()
    private val settingsRepository: SettingsRepository = mock()
    private val calculator: SessionCalculator = mock()
    private val dateFormatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

    @Before
    fun setUp() {
        repository = LocalStudyRepository(studyDao, settingsRepository, calculator)
    }

    @Test
    fun `completeSession calculates progress and updates DAO`() = runTest {
        // Given
        val deck = Deck(1, "Test", "Desc", cardCount = 10)
        val today = dateFormatter.format(Calendar.getInstance().time)
        val settings = UserSettings(dailyStudyGoal = 50)
        
        whenever(studyDao.getRecentActivity()).thenReturn(flowOf(emptyList()))
        whenever(studyDao.getDeckMastery(deck.id)).thenReturn(null)
        whenever(settingsRepository.getSettings()).thenReturn(flowOf(settings))
        whenever(studyDao.getDailyActivity(today)).thenReturn(null)
        
        val sessionResult = SessionResult(10.0, 10.0, MasteryLevel.NOVICE)
        whenever(calculator.calculateProgress(any(), any(), any(), any())).thenReturn(sessionResult)

        // When
        val result = repository.completeSession(deck, 5, 0L, 100L, 100L)

        // Then
        assertEquals(sessionResult, result)
        verify(studyDao).completeSession(any(), any(), any())
    }

    @Test
    fun `getCurrentStreak returns correct streak when goal met today`() = runTest {
        // Given
        val calendar = Calendar.getInstance()
        val today = dateFormatter.format(calendar.time)
        calendar.add(Calendar.DAY_OF_YEAR, -1)
        val yesterday = dateFormatter.format(calendar.time)
        
        val activities = listOf(
            DailyActivity(today, 50, true),
            DailyActivity(yesterday, 50, true)
        )
        whenever(studyDao.getRecentActivity()).thenReturn(flowOf(activities))

        // When
        val streak = repository.getCurrentStreak()

        // Then
        assertEquals(2, streak)
    }
}
