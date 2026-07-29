package com.saishaddai.flashcards.viewmodel

import android.app.Application
import com.saishaddai.flashcards.model.Deck
import com.saishaddai.flashcards.repository.DeckRepository
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
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.mock

@OptIn(ExperimentalCoroutinesApi::class)
class DecksViewModelTest {
    private val testDispatcher = StandardTestDispatcher()

    private lateinit var viewModel: DecksViewModel
    private lateinit var repository: FakeDeckRepository
    private val application: Application = mock()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        repository = FakeDeckRepository()
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `initial state is correct and loads decks`() = runTest {
        viewModel = DecksViewModel(application, repository)
        
        assertTrue(viewModel.uiState.value is UiState.Loading)
        
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertTrue(state is UiState.Success)
        val data = (state as UiState.Success).data
        assertEquals(2, data.decks.size)
        assertFalse(data.showEmptyDeckDialog)
    }

    @Test
    fun `loadDecks handles error from repository`() = runTest {
        repository.shouldFail = true
        viewModel = DecksViewModel(application, repository)
        
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertTrue(state is UiState.Error)
        assertEquals("Critical Error:\nRuntimeException\nRepository failure", (state as UiState.Error).message)
    }

    @Test
    fun `onDeckSelected updates selected state`() = runTest {
        viewModel = DecksViewModel(application, repository)
        advanceUntilIdle()

        val currentState = (viewModel.uiState.value as UiState.Success).data
        val secondDeck = currentState.decks[1]
        viewModel.onDeckSelected(secondDeck)

        val updatedState = (viewModel.uiState.value as UiState.Success).data
        assertTrue(updatedState.decks[1].isSelected)
        assertFalse(updatedState.decks[0].isSelected)
    }

    @Test
    fun `getRandomDeck returns a deck when available`() = runTest {
        viewModel = DecksViewModel(application, repository)
        advanceUntilIdle()

        val deck = viewModel.getRandomDeck()
        assertNotNull(deck)
    }

    @Test
    fun `getRandomDeck returns null when no decks available`() = runTest {
        repository.decks = emptyList()
        viewModel = DecksViewModel(application, repository)
        advanceUntilIdle()

        val deck = viewModel.getRandomDeck()
        assertNull(deck)
    }

    @Test
    fun `onStartSession shows dialog when decks are empty`() = runTest {
        repository.decks = emptyList()
        viewModel = DecksViewModel(application, repository)
        advanceUntilIdle()

        viewModel.onStartSession()
        val state = (viewModel.uiState.value as UiState.Success).data
        assertTrue(state.showEmptyDeckDialog)
    }

    @Test
    fun `onStartSession does not show dialog when decks are not empty`() = runTest {
        viewModel = DecksViewModel(application, repository)
        advanceUntilIdle()

        viewModel.onStartSession()
        val state = (viewModel.uiState.value as UiState.Success).data
        assertFalse(state.showEmptyDeckDialog)
    }

    @Test
    fun `dismissEmptyDeckDialog hides dialog`() = runTest {
        repository.decks = emptyList()
        viewModel = DecksViewModel(application, repository)
        advanceUntilIdle()

        viewModel.onStartSession()
        assertTrue((viewModel.uiState.value as UiState.Success).data.showEmptyDeckDialog)

        viewModel.dismissEmptyDeckDialog()
        assertFalse((viewModel.uiState.value as UiState.Success).data.showEmptyDeckDialog)
    }

    class FakeDeckRepository: DeckRepository<Deck> {
        var decks = listOf(
            Deck(1, "Deck1", "Deck1 LN", cardCount = 10),
            Deck(2, "Deck2", "Deck2 LN", cardCount = 15)
        )
        var shouldFail = false

        override suspend fun getData(): List<Deck> {
            if (shouldFail) throw RuntimeException("Repository failure")
            return decks
        }
    }
}
