package com.saishaddai.flashcards.viewmodel

import android.app.Application
import com.saishaddai.flashcards.model.Deck
import com.saishaddai.flashcards.repository.DeckRepository
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
        
        assertTrue(viewModel.isLoading.value)
        
        advanceUntilIdle()

        assertFalse(viewModel.isLoading.value)
        assertEquals(2, viewModel.decks.value.size)
        assertFalse(viewModel.showEmptyDeckDialog.value)
    }

    @Test
    fun `onDeckSelected updates selected state`() = runTest {
        viewModel = DecksViewModel(application, repository)
        advanceUntilIdle()

        val secondDeck = viewModel.decks.value[1]
        viewModel.onDeckSelected(secondDeck)

        assertTrue(viewModel.decks.value[1].isSelected)
        assertFalse(viewModel.decks.value[0].isSelected)
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
        assertTrue(viewModel.showEmptyDeckDialog.value)
    }

    @Test
    fun `onStartSession does not show dialog when decks are not empty`() = runTest {
        viewModel = DecksViewModel(application, repository)
        advanceUntilIdle()

        viewModel.onStartSession()
        assertFalse(viewModel.showEmptyDeckDialog.value)
    }

    @Test
    fun `dismissEmptyDeckDialog hides dialog`() = runTest {
        repository.decks = emptyList()
        viewModel = DecksViewModel(application, repository)
        advanceUntilIdle()

        viewModel.onStartSession()
        assertTrue(viewModel.showEmptyDeckDialog.value)

        viewModel.dismissEmptyDeckDialog()
        assertFalse(viewModel.showEmptyDeckDialog.value)
    }

    class FakeDeckRepository: DeckRepository<Deck> {
        var decks = listOf(
            Deck(1, "Deck1", "Deck1 LN"),
            Deck(2, "Deck2", "Deck2 LN")
        )

        override suspend fun getData(): List<Deck> {
            return decks
        }
    }
}
