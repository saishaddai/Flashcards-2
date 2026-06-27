package com.saishaddai.flashcards.screens

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.doubleClick
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollToNode
import androidx.compose.ui.test.performTouchInput
import androidx.test.platform.app.InstrumentationRegistry
import com.saishaddai.flashcards.R
import com.saishaddai.flashcards.model.Deck
import com.saishaddai.flashcards.utils.TestTags
import com.saishaddai.flashcards.utils.UiState
import org.junit.Rule
import org.junit.Test

class DeckListScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val context = InstrumentationRegistry.getInstrumentation().targetContext

    @Test
    fun testDeckListScreen_isLoading_showsLoader() {
        composeTestRule.setContent {
            DeckListScreen(
                uiState = UiState.Loading,
                quickStartEnabled = false,
                onDeckSelected = {},
                onStartSessionClick = {},
                onDismissEmptyDeckDialog = {},
                onTriggerEmptyDeckDialog = {},
                onRetry = {}
            )
        }

        composeTestRule.onNodeWithTag(TestTags.FULL_LOADER).assertIsDisplayed()
        composeTestRule.onNodeWithText(context.getString(R.string.loading_decks)).assertIsDisplayed()
    }

    @Test
    fun testDeckListContent_displaysMockDecks() {
        val mockDecks = listOf(
            Deck(id = 1, name = "Mock Deck 1", longName = "Mock Deck 1 Long", cardCount = 10),
            Deck(id = 2, name = "Mock Deck 2", longName = "Mock Deck 2 Long", cardCount = 5)
        )

        composeTestRule.setContent {
            DeckListContent(
                decks = mockDecks,
                showEmptyDeckDialogState = false,
                quickStartEnabled = false,
                onDeckSelected = {},
                onStartSessionClick = {},
                onDismissEmptyDeckDialog = {},
                onTriggerEmptyDeckDialog = {}
            )
        }

        composeTestRule.onNodeWithText("Mock Deck 1").assertIsDisplayed()
        composeTestRule.onNodeWithText("Mock Deck 2").assertIsDisplayed()
    }

    @Test
    fun testDeckListContent_emptyMockDeck_triggersDialogCallback() {
        var triggerDialogCalled = false
        val mockDecks = listOf(
            Deck(
                id = 1,
                name = "Empty Deck",
                longName = "Empty Deck Long",
                cardCount = 0,
                isSelected = true
            )
        )

        composeTestRule.setContent {
            DeckListContent(
                decks = mockDecks,
                showEmptyDeckDialogState = false,
                quickStartEnabled = false,
                onDeckSelected = {},
                onStartSessionClick = {},
                onDismissEmptyDeckDialog = {},
                onTriggerEmptyDeckDialog = { triggerDialogCalled = true }
            )
        }

        composeTestRule.onNodeWithText(context.getString(R.string.decks_start_session_button))
            .performClick()

        assert(triggerDialogCalled)
    }

    @Test
    fun testDeckListContent_clickMockDeck_triggersCallBack() {
        var onStartSessionClickCalled = false
        val mockDecks = listOf(
            Deck(
                id = 1,
                name = "Mock Deck",
                longName = "Mock Deck Long",
                cardCount = 1,
                isSelected = true
            )
        )

        composeTestRule.setContent {
            DeckListContent(
                decks = mockDecks,
                showEmptyDeckDialogState = false,
                quickStartEnabled = false,
                onDeckSelected = {},
                onStartSessionClick = { onStartSessionClickCalled = true },
                onDismissEmptyDeckDialog = {},
                onTriggerEmptyDeckDialog = {}
            )
        }

        composeTestRule.onNodeWithText(context.getString(R.string.decks_start_session_button))
            .performClick()

        assert(onStartSessionClickCalled)
    }

    @Test
    fun testDeckListContent_deckCount_displaysCorrectText() {
        val mockDecks = listOf(
            Deck(id = 1, name = "Regular Deck", longName = "Regular", cardCount = 50, mastery = 20),
            Deck(id = 2, name = "Large Deck", longName = "Large", cardCount = 150, mastery = 80)
        )

        composeTestRule.setContent {
            DeckListContent(
                decks = mockDecks,
                showEmptyDeckDialogState = false,
                quickStartEnabled = false,
                onDeckSelected = {},
                onStartSessionClick = {},
                onDismissEmptyDeckDialog = {},
                onTriggerEmptyDeckDialog = {}
            )
        }

        // Regular case: 50 Cards • 20%
        val expectedRegularText = context.getString(R.string.decks_card_count, 50, 20)
        composeTestRule.onNodeWithText(expectedRegularText).assertIsDisplayed()

        // 99+ case: 99+ Cards • 80%
        val expectedLargeText = context.getString(R.string.decks_card_count_plus, 80)
        composeTestRule.onNodeWithText(expectedLargeText).assertIsDisplayed()
    }

    @Test
    fun testDeckListContent_doubleClickMockDeck_triggersDialogIfQuickStartDisabled() {
        val mockDecks = listOf(
            Deck(id = 1, name = "Double Click Deck", longName = "Double Click", cardCount = 10)
        )

        composeTestRule.setContent {
            DeckListContent(
                decks = mockDecks,
                showEmptyDeckDialogState = false,
                quickStartEnabled = false,
                onDeckSelected = {},
                onStartSessionClick = {},
                onDismissEmptyDeckDialog = {},
                onTriggerEmptyDeckDialog = {}
            )
        }

        // Double-click the deck title
        composeTestRule.onNodeWithText("Double Click Deck").performTouchInput {
            doubleClick()
        }

        // Verify the confirmation dialog is displayed
        composeTestRule.onNodeWithText(context.getString(R.string.flashcard_start_dialog_title))
            .assertIsDisplayed()
    }

    @Test
    fun testDeckListContent_doubleClickMockDeck_quickStartEnabled_skipsDialog() {
        var onStartSessionClickCalled = false
        val mockDecks = listOf(
            Deck(id = 1, name = "Quick Start Deck", longName = "Quick Start", cardCount = 10)
        )

        composeTestRule.setContent {
            DeckListContent(
                decks = mockDecks,
                showEmptyDeckDialogState = false,
                quickStartEnabled = true,
                onDeckSelected = {},
                onStartSessionClick = { onStartSessionClickCalled = true },
                onDismissEmptyDeckDialog = {},
                onTriggerEmptyDeckDialog = {}
            )
        }

        // Double-click the deck title
        composeTestRule.onNodeWithText("Quick Start Deck").performTouchInput {
            doubleClick()
        }

        // Verify the confirmation dialog is NOT displayed
        composeTestRule.onNodeWithText(context.getString(R.string.flashcard_start_dialog_title))
            .assertDoesNotExist()

        // Verify callback was triggered immediately
        assert(onStartSessionClickCalled)
    }

    @Test
    fun testDeckListContent_emptyDecks_showsEmptyStateMessage() {
        composeTestRule.setContent {
            DeckListContent(
                decks = emptyList(),
                showEmptyDeckDialogState = false,
                quickStartEnabled = false,
                onDeckSelected = {},
                onStartSessionClick = {},
                onDismissEmptyDeckDialog = {},
                onTriggerEmptyDeckDialog = {}
            )
        }

        // Verify the empty state message is displayed
        composeTestRule.onNodeWithTag(TestTags.DECKS_EMPTY_STATE).assertIsDisplayed()
        composeTestRule.onNodeWithText(context.getString(R.string.no_decks_available))
            .assertIsDisplayed()
    }

    @Test
    fun testDeckListContent_noDeckSelected_startButtonDisabled() {
        val mockDecks = listOf(
            Deck(
                id = 1,
                name = "Deck 1",
                longName = "Deck 1 Long",
                cardCount = 10,
                isSelected = false
            ),
            Deck(
                id = 2,
                name = "Deck 2",
                longName = "Deck 2 Long",
                cardCount = 5,
                isSelected = false
            )
        )

        composeTestRule.setContent {
            DeckListContent(
                decks = mockDecks,
                showEmptyDeckDialogState = false,
                quickStartEnabled = false,
                onDeckSelected = {},
                onStartSessionClick = {},
                onDismissEmptyDeckDialog = {},
                onTriggerEmptyDeckDialog = {}
            )
        }

        // Verify the start button is disabled
        composeTestRule.onNodeWithTag(TestTags.DECKS_LIST_START_BUTTON).assertIsNotEnabled()
    }

    @Test
    fun testDeckListContent_deckSelected_startButtonEnabled() {
        val mockDecks = listOf(
            Deck(
                id = 1,
                name = "Deck 1",
                longName = "Deck 1 Long",
                cardCount = 10,
                isSelected = true
            ),
            Deck(
                id = 2,
                name = "Deck 2",
                longName = "Deck 2 Long",
                cardCount = 5,
                isSelected = false
            )
        )

        composeTestRule.setContent {
            DeckListContent(
                decks = mockDecks,
                showEmptyDeckDialogState = false,
                quickStartEnabled = false,
                onDeckSelected = {},
                onStartSessionClick = {},
                onDismissEmptyDeckDialog = {},
                onTriggerEmptyDeckDialog = {}
            )
        }

        // Verify the start button is enabled
        composeTestRule.onNodeWithTag(TestTags.DECKS_LIST_START_BUTTON).assertIsEnabled()
    }

    @Test
    fun testDeckListContent_longList_isScrollable() {
        val mockDecks = List(20) { i ->
            Deck(
                id = i + 1,
                name = "Deck ${i + 1}",
                longName = "Deck ${i + 1} Long",
                cardCount = 10
            )
        }

        composeTestRule.setContent {
            DeckListContent(
                decks = mockDecks,
                showEmptyDeckDialogState = false,
                quickStartEnabled = false,
                onDeckSelected = {},
                onStartSessionClick = {},
                onDismissEmptyDeckDialog = {},
                onTriggerEmptyDeckDialog = {}
            )
        }

        // Try to scroll to the last item using the grid and performScrollToNode
        composeTestRule.onNodeWithTag(TestTags.DECKS_LIST_GRID)
            .performScrollToNode(hasText("Deck 20"))
        
        composeTestRule.onNodeWithText("Deck 20").assertIsDisplayed()
    }
}
