package com.saishaddai.flashcards.viewmodel

import android.app.Application
import com.saishaddai.flashcards.model.DeckType
import com.saishaddai.flashcards.model.Flashcard
import com.saishaddai.flashcards.repository.FlashcardRepository
import com.saishaddai.flashcards.repository.SettingsRepository
import com.saishaddai.flashcards.repository.UserSettings
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

        assertEquals(2, viewModel.flashcards.value.size)
        assertFalse(viewModel.showAnswer.value)
        assertFalse(viewModel.isFinished.value)
    }

    @Test
    fun `onShowResponseClicked updates showAnswer state`() {
        assertFalse(viewModel.showAnswer.value)
        viewModel.onShowResponseClicked()
        assertTrue(viewModel.showAnswer.value)
    }

    @Test
    fun `onFinishSession updates isFinished state`() {
        assertFalse(viewModel.isFinished.value)
        viewModel.onFinishSession()
        assertTrue(viewModel.isFinished.value)
    }

    @Test
    fun `onPageChanged resets showAnswer state based on settings`() = runTest {
        viewModel.onShowResponseClicked()
        assertTrue(viewModel.showAnswer.value)

        viewModel.onPageChanged()
        advanceUntilIdle()
        assertFalse(viewModel.showAnswer.value)
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
