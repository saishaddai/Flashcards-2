package com.saishaddai.flashcards.viewmodel

import android.app.Application
import com.saishaddai.flashcards.model.DeckType
import com.saishaddai.flashcards.model.Flashcard
import com.saishaddai.flashcards.repository.FlashcardRepository
import com.saishaddai.flashcards.repository.SettingsRepository
import com.saishaddai.flashcards.repository.UserSettings
import com.saishaddai.flashcards.utils.UiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
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
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock

@OptIn(ExperimentalCoroutinesApi::class)
class FlashcardViewModelTest {

    private val testDispatcher = StandardTestDispatcher()

    private lateinit var viewModel: FlashcardViewModel
    private lateinit var repository: FakeFlashcardRepository
    private val settingsRepository: SettingsRepository = mock {
        on { getSettings() } doReturn flowOf(UserSettings(showAnswers = false))
    }
    private val application: Application = mock()
    private val deckId = 1

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        repository = FakeFlashcardRepository()
        viewModel = FlashcardViewModel(application, deckId, repository, settingsRepository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `initial state is correct`() = runTest {
        // Wait for loadFlashcards to finish
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertTrue(state is UiState.Success)
        val data = (state as UiState.Success).data
        assertEquals(2, data.flashcards.size)
        assertFalse(data.showAnswer)
        assertFalse(data.isFinished)
    }

    @Test
    fun `onShowResponseClicked updates showAnswer state`() = runTest {
        advanceUntilIdle()
        assertFalse((viewModel.uiState.value as UiState.Success).data.showAnswer)
        viewModel.onShowResponseClicked()
        assertTrue((viewModel.uiState.value as UiState.Success).data.showAnswer)
    }

    @Test
    fun `onFinishSession updates isFinished state`() = runTest {
        advanceUntilIdle()
        assertFalse((viewModel.uiState.value as UiState.Success).data.isFinished)
        viewModel.onFinishSession(0)
        assertTrue((viewModel.uiState.value as UiState.Success).data.isFinished)
    }

    @Test
    fun `onPageChanged resets showAnswer state based on settings`() = runTest {
        advanceUntilIdle()
        viewModel.onShowResponseClicked()
        assertTrue((viewModel.uiState.value as UiState.Success).data.showAnswer)

        viewModel.onPageChanged(0)
        advanceUntilIdle()
        assertFalse((viewModel.uiState.value as UiState.Success).data.showAnswer)
    }

    // A simple fake repository for testing
    class FakeFlashcardRepository : FlashcardRepository<DeckType, Flashcard> {
        override suspend fun getData(type: DeckType, size: Int): List<Flashcard> {
            return listOf(
                Flashcard(1, 1, "Q1", "A1"),
                Flashcard(1, 2, "Q2", "A2")
            )
        }

        override suspend fun getDataCount(type: DeckType): Int {
            return 2
        }
    }
}
