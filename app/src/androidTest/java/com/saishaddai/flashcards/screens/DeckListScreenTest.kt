package com.saishaddai.flashcards.screens

import android.app.Application
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.platform.app.InstrumentationRegistry
import com.saishaddai.flashcards.R
import com.saishaddai.flashcards.model.Deck
import com.saishaddai.flashcards.repository.DeckRepository
import com.saishaddai.flashcards.utils.TestTags
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
        composeTestRule.onNodeWithTag(TestTags.FULL_LOADER).assertIsDisplayed()

        // Wait for loader to disappear
        composeTestRule.waitUntil(5000) {
            composeTestRule.onAllNodesWithTag(TestTags.FULL_LOADER).fetchSemanticsNodes().isEmpty()
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

    @Test
    fun testDeckListScreen_deckCount_displaysCorrectText() {
        val mockDecks = listOf(
            Deck(id = 1, name = "Regular Deck", longName = "Regular", cardCount = 50, mastery = 20),
            Deck(id = 2, name = "Large Deck", longName = "Large", cardCount = 150, mastery = 80)
        )

        val fakeRepository = object : DeckRepository<Deck> {
            override suspend fun getData(): List<Deck> = mockDecks
        }

        val viewModel = DecksViewModel(application, fakeRepository)

        composeTestRule.setContent {
            DeckListScreen(viewModel = viewModel, onStartSessionClick = {})
        }

        // Regular case: 50 Cards • 20%
        val expectedRegularText = context.getString(R.string.decks_card_count, 50, 20)
        composeTestRule.onNodeWithText(expectedRegularText).assertIsDisplayed()

        // 99+ case: 99+ Cards • 80%
        val expectedLargeText = context.getString(R.string.decks_card_count_plus, 80)
        composeTestRule.onNodeWithText(expectedLargeText).assertIsDisplayed()
    }

    @Test
    fun testDeckListScreen_deckTitle_isDisplayedAndHasCorrectText() {
        val longTitle = "A very long deck title that should definitely be ellipsized"
        val mockDecks = listOf(
            Deck(id = 1, name = longTitle, longName = "Long", cardCount = 10)
        )

        val fakeRepository = object : DeckRepository<Deck> {
            override suspend fun getData(): List<Deck> = mockDecks
        }

        val viewModel = DecksViewModel(application, fakeRepository)

        composeTestRule.setContent {
            DeckListScreen(viewModel = viewModel, onStartSessionClick = {})
        }

        // Check by text directly as it should be in the hierarchy even if ellipsized visually
        composeTestRule.onNodeWithText(longTitle).assertIsDisplayed()
        
        // Also verify it has the correct tag
        composeTestRule.onNodeWithTag(TestTags.DECKS_LIST_DECK_TITLE).assertIsDisplayed()
    }
}
