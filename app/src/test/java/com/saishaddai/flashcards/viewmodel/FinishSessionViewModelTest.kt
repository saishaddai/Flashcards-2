package com.saishaddai.flashcards.viewmodel

import com.saishaddai.flashcards.model.Deck
import com.saishaddai.flashcards.model.MasteryLevel
import com.saishaddai.flashcards.repository.StudyRepository
import com.saishaddai.flashcards.utils.SessionResult
import com.saishaddai.flashcards.utils.UiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

@OptIn(ExperimentalCoroutinesApi::class)
class FinishSessionViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var viewModel: FinishSessionViewModel
    private val studyRepository: StudyRepository = mock()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        viewModel = FinishSessionViewModel(studyRepository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `saveSession success updates uiState`() = runTest {
        val deck = Deck(1, "Test", "Test")
        val result = SessionResult(10.0, 50.0, MasteryLevel.EXPERIENCED)
        whenever(studyRepository.completeSession(any(), any(), any(), any(), any())).thenReturn(result)

        viewModel.saveSession(deck, 10, 0, 1000, 1000)
        assertTrue(viewModel.uiState.value is UiState.Loading)
        
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertTrue(state is UiState.Success)
        assertEquals(result, (state as UiState.Success).data.sessionResult)
    }

    @Test
    fun `saveSession error updates uiState`() = runTest {
        val deck = Deck(1, "Test", "Test")
        whenever(studyRepository.completeSession(any(), any(), any(), any(), any())).thenThrow(RuntimeException("Error"))

        viewModel.saveSession(deck, 10, 0, 1000, 1000)
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertTrue(state is UiState.Error)
        assertEquals("Failed to save session", (state as UiState.Error).message)
    }

    @Test
    fun `onBackToDecksClicked sets navigation flag`() = runTest {
        // First get to Success state
        val deck = Deck(1, "Test", "Test")
        val result = SessionResult(10.0, 50.0, MasteryLevel.EXPERIENCED)
        whenever(studyRepository.completeSession(any(), any(), any(), any(), any())).thenReturn(result)
        viewModel.saveSession(deck, 10, 0, 1000, 1000)
        advanceUntilIdle()

        viewModel.onBackToDecksClicked()
        val state = (viewModel.uiState.value as UiState.Success).data
        assertTrue(state.navigateToDeckList)
    }

    @Test
    fun `onNavigationHandled clears navigation flag`() = runTest {
        // First get to Success state with flag set
        val deck = Deck(1, "Test", "Test")
        val result = SessionResult(10.0, 50.0, MasteryLevel.EXPERIENCED)
        whenever(studyRepository.completeSession(any(), any(), any(), any(), any())).thenReturn(result)
        viewModel.saveSession(deck, 10, 0, 1000, 1000)
        advanceUntilIdle()
        viewModel.onBackToDecksClicked()

        viewModel.onNavigationHandled()
        val state = (viewModel.uiState.value as UiState.Success).data
        assertFalse(state.navigateToDeckList)
    }
}
