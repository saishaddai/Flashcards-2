package com.saishaddai.flashcards.viewmodel

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
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class DecksViewModelTest {
    private val testDispatcher = StandardTestDispatcher()

    private lateinit var viewModel: DecksViewModel
    private lateinit var repository: FakeDeckRepository

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
    fun `initial state is correct`() = runTest {
        // Wait for loadFlashcards to finish
        advanceUntilIdle()

        assertEquals(2, viewModel.decks.value.size)
//        assertFalse(viewModel.selectedDeck.value) //TODO: selectedDeck must be living in the viewModel
//        assertFalse(viewModel.showEmptyDeckDialog.value) //TODO showEmptyDialog should also living in viewModel
    }

    @Test
    fun `onSelectDeckClicked updates selectedDeck state`() {
        //assertFalse(viewModel.selectedDeck.value == 1)
        //viewModel.onSelectDeckClicked(2)
        //assertTrue(viewModel.selectedDeck.value == 1)
    }

    @Test
    fun `onStartSession empty deck updates showEmptyDeckDialog state`() {
//        assertFalse(viewModel.showEmptyDeckDialog.value)
//        viewModel.onStartSession()
//        assertTrue(viewModel.showEmptyDeckDialog.value)
    }

    class FakeDeckRepository: DeckRepository<Deck> {
        override suspend fun getData(): List<Deck> {
            return listOf(
                Deck(1, "Deck1", "Deck1 LN"),
                Deck(2, "Deck2", "Deck2 LN")
            )
        }
    }
}