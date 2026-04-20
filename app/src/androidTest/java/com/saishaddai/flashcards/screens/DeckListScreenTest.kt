package com.saishaddai.flashcards.screens

import android.app.Application
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.platform.app.InstrumentationRegistry
import com.saishaddai.flashcards.R
import com.saishaddai.flashcards.model.Deck
import com.saishaddai.flashcards.repository.DeckRepository
import com.saishaddai.flashcards.viewmodel.DecksViewModel
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.mock

class DeckListScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val context = InstrumentationRegistry.getInstrumentation().targetContext
    private val application: Application = mock()

    @Test
    fun testDeckListScreen_initialState_showsLoaderThenDecks() {
        val mockDecks = listOf(
            Deck(id = 1, name = "Mock Deck 1", longName = "Mock Deck 1 Long", cardCount = 10)
        )

        val fakeRepository = object : DeckRepository<Deck> {
            override suspend fun getData(): List<Deck> {
                kotlinx.coroutines.delay(500) // Simulate loading
                return mockDecks
            }
        }

        val viewModel = DecksViewModel(application, fakeRepository)

        composeTestRule.setContent {
            DeckListScreen(
                viewModel = viewModel,
                onStartSessionClick = {}
            )
        }

        // Check if loader is shown initially
        composeTestRule.onNodeWithTag("full_loader").assertIsDisplayed()

        // Wait for loader to disappear
        composeTestRule.waitUntil(5000) {
            composeTestRule.onAllNodes(hasTestTag("full_loader")).fetchSemanticsNodes().isEmpty()
        }

        composeTestRule.onNodeWithText(context.getString(R.string.decks_welcome)).assertIsDisplayed()
        composeTestRule.onNodeWithText("Mock Deck 1").assertIsDisplayed()
    }

    @Test
    fun testDeckListScreen_displaysMockDecks() {
        val mockDecks = listOf(
            Deck(id = 1, name = "Mock Deck 1", longName = "Mock Deck 1 Long", cardCount = 10),
            Deck(id = 2, name = "Mock Deck 2", longName = "Mock Deck 2 Long", cardCount = 5)
        )

        val fakeRepository = object : DeckRepository<Deck> {
            override suspend fun getData(): List<Deck> = mockDecks
        }

        val viewModel = DecksViewModel(application, fakeRepository)

        composeTestRule.setContent {
            DeckListScreen(
                viewModel = viewModel,
                onStartSessionClick = {}
            )
        }

        composeTestRule.onNodeWithText("Mock Deck 1").assertIsDisplayed()
        composeTestRule.onNodeWithText("Mock Deck 2").assertIsDisplayed()
    }

    @Test
    fun testDeckListScreen_emptyMockDeck_opensDialogThatCanBeDismissed() {
        val mockDecks = listOf(
            Deck(id = 1, name = "Empty Deck", longName = "Empty Deck Long", cardCount = 0, isSelected = true)
        )

        val fakeRepository = object : DeckRepository<Deck> {
            override suspend fun getData(): List<Deck> = mockDecks
        }

        val viewModel = DecksViewModel(application, fakeRepository)

        composeTestRule.setContent {
            DeckListScreen(viewModel = viewModel, onStartSessionClick = {})
        }

        composeTestRule.onNodeWithText(context.getString(R.string.decks_start_session_button)).performClick()

        // Check dialog
        composeTestRule.onNodeWithText(context.getString(R.string.empty_deck_confirm)).assertIsDisplayed()
            .performClick().assertDoesNotExist()
    }

    @Test
    fun testDeckListScreen_clickMockDeck_triggersCallBack() {
        var onStartSessionClickCalled = false
        val mockDecks = listOf(
            Deck(id = 1, name = "Mock Deck", longName = "Mock Deck Long", cardCount = 1, isSelected = true)
        )

        val fakeRepository = object : DeckRepository<Deck> {
            override suspend fun getData(): List<Deck> = mockDecks
        }

        val viewModel = DecksViewModel(application, fakeRepository)

        composeTestRule.setContent {
            DeckListScreen(viewModel = viewModel, onStartSessionClick = { onStartSessionClickCalled = true })
        }

        composeTestRule.onNodeWithText(context.getString(R.string.decks_start_session_button)).performClick()

        composeTestRule.waitUntil(5000) { onStartSessionClickCalled }
    }
}
