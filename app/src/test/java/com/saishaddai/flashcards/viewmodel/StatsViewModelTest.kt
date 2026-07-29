package com.saishaddai.flashcards.viewmodel

import androidx.compose.ui.graphics.Color
import com.saishaddai.flashcards.repository.StatsRepository
import com.saishaddai.flashcards.screens.MasteryData
import com.saishaddai.flashcards.utils.UiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

@OptIn(ExperimentalCoroutinesApi::class)
class StatsViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var viewModel: StatsViewModel
    private val repository: StatsRepository = mock()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        
        whenever(repository.getWeeklyActivity()) doReturn flowOf(emptyList())
        whenever(repository.getSkillMastery()) doReturn flowOf(emptyList())
        whenever(repository.getFlashcardsViewed()) doReturn flowOf("0")
        whenever(repository.getCurrentStreak()) doReturn flowOf("0 Days")
        whenever(repository.getStudyTime()) doReturn flowOf("0m")
        whenever(repository.getMasteredDecks()) doReturn flowOf("0%")
        whenever(repository.getWeeklyComparison()) doReturn flowOf(0)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `initial state is Loading`() = runTest {
        viewModel = StatsViewModel(repository)
        assertTrue(viewModel.uiState.value is UiState.Loading)
    }

    @Test
    fun `loadStats success updates uiState`() = runTest {
        val weekly = listOf(1, 2, 3, 4, 5, 6, 7)
        val skills = listOf(MasteryData("Title", 50, 0, Color.Blue))
        
        whenever(repository.getWeeklyActivity()) doReturn flowOf(weekly)
        whenever(repository.getSkillMastery()) doReturn flowOf(skills)
        whenever(repository.getFlashcardsViewed()) doReturn flowOf("100")
        
        viewModel = StatsViewModel(repository)
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertTrue(state is UiState.Success)
        val data = (state as UiState.Success).data
        assertEquals(weekly, data.weeklyActivity)
        assertEquals(skills, data.skillMastery)
        assertEquals("100", data.flashcardsViewed)
    }

    @Test
    fun `loadStats error updates uiState`() = runTest {
        whenever(repository.getWeeklyActivity()) doReturn flow { throw RuntimeException("Error") }
        
        viewModel = StatsViewModel(repository)
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertTrue(state is UiState.Error)
        assertEquals("Failed to load statistics", (state as UiState.Error).message)
    }

    @Test
    fun `onViewAllSkillsClicked toggles expansion`() = runTest {
        viewModel = StatsViewModel(repository)
        advanceUntilIdle()

        val initialState = (viewModel.uiState.value as UiState.Success).data
        assertFalse(initialState.isSkillsExpanded)

        viewModel.onViewAllSkillsClicked()
        val expandedState = (viewModel.uiState.value as UiState.Success).data
        assertTrue(expandedState.isSkillsExpanded)

        viewModel.onViewAllSkillsClicked()
        val collapsedState = (viewModel.uiState.value as UiState.Success).data
        assertFalse(collapsedState.isSkillsExpanded)
    }

    @Test
    fun `onInfoClick shows dialog content`() = runTest {
        viewModel = StatsViewModel(repository)
        advanceUntilIdle()

        viewModel.onInfoClick("Title", "Description")
        val state = (viewModel.uiState.value as UiState.Success).data
        assertEquals("Title", state.infoDialogContent?.first)
        assertEquals("Description", state.infoDialogContent?.second)
    }

    @Test
    fun `onDismissInfoDialog clears dialog content`() = runTest {
        viewModel = StatsViewModel(repository)
        advanceUntilIdle()

        viewModel.onInfoClick("Title", "Description")
        viewModel.onDismissInfoDialog()
        val state = (viewModel.uiState.value as UiState.Success).data
        assertNull(state.infoDialogContent)
    }
}
