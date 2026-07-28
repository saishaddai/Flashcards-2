package com.saishaddai.flashcards.repository.impl

import com.saishaddai.flashcards.data.local.StudyDao
import com.saishaddai.flashcards.model.DailyActivity
import com.saishaddai.flashcards.model.DeckMastery
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class LocalStatsRepositoryTest {

    private lateinit var repository: LocalStatsRepository
    private val studyDao: StudyDao = mock()
    private val dateFormatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

    @Before
    fun setUp() {
        repository = LocalStatsRepository(studyDao)
    }

    @Test
    fun `getWeeklyActivity returns cards reviewed for each day of current week`() = runTest {
        // Given
        val calendar = Calendar.getInstance()
        calendar.firstDayOfWeek = Calendar.MONDAY
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY)
        val monday = dateFormatter.format(calendar.time)
        
        val activities = listOf(
            DailyActivity(monday, 10, true)
        )
        whenever(studyDao.getRecentActivity()).thenReturn(flowOf(activities))

        // When
        val result = repository.getWeeklyActivity().first()

        // Then
        assertEquals(10, result[0])
        assertEquals(0, result[1]) // Tuesday
    }

    @Test
    fun `getFlashcardsViewed returns sum of all reviews`() = runTest {
        // Given
        val activities = listOf(
            DailyActivity("2023-10-01", 10, true),
            DailyActivity("2023-10-02", 20, true)
        )
        whenever(studyDao.getRecentActivity()).thenReturn(flowOf(activities))

        // When
        val result = repository.getFlashcardsViewed().first()

        // Then
        assertEquals("30", result)
    }

    @Test
    fun `getMasteredDecks returns correct percentage`() = runTest {
        // Given
        val mastery = listOf(
            DeckMastery(1, "Deck 1", 85.0, "MASTERED", 0),
            DeckMastery(2, "Deck 2", 50.0, "LEARNING", 0)
        )
        whenever(studyDao.getAllDeckMastery()).thenReturn(flowOf(mastery))

        // When
        val result = repository.getMasteredDecks().first()

        // Then
        assertEquals("50%", result)
    }
}
