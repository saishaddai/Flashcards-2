package com.saishaddai.flashcards.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.assertIsNotSelected
import androidx.compose.ui.test.assertIsSelected
import androidx.compose.ui.test.doubleClick
import androidx.compose.ui.test.hasClickAction
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
    fun testDeckListScreen_isError_showsErrorView() {
        val errorMessage = "Test Error Message"
        composeTestRule.setContent {
            DeckListScreen(
                uiState = UiState.Error(errorMessage),
                quickStartEnabled = false,
                onDeckSelected = {},
                onStartSessionClick = {},
                onDismissEmptyDeckDialog = {},
                onTriggerEmptyDeckDialog = {},
                onRetry = {}
            )
        }

        composeTestRule.onNodeWithText("Oops!").assertIsDisplayed()
        composeTestRule.onNodeWithText(errorMessage).assertIsDisplayed()
    }

    @Test
    fun testDeckListContent_clickDeck_triggersSelection() {
        var selectedDeck: Deck? = null
        val mockDecks = listOf(
            Deck(id = 1, name = "Selection Test Deck", longName = "Selection Test Deck Long", cardCount = 10)
        )

        composeTestRule.setContent {
            DeckListContent(
                decks = mockDecks,
                showEmptyDeckDialogState = false,
                quickStartEnabled = false,
                onDeckSelected = { selectedDeck = it },
                onStartSessionClick = {},
                onDismissEmptyDeckDialog = {},
                onTriggerEmptyDeckDialog = {}
            )
        }

        // Find the clickable card that contains the deck name and perform click
        composeTestRule.onNode(hasText("Selection Test Deck") and hasClickAction())
            .performClick()

        composeTestRule.waitForIdle()
        assert(selectedDeck?.id == 1)
    }

    @Test
    fun testDeckListContent_showEmptyDeckDialog_isVisibleAndDismisses() {
        var dismissCalled = false
        composeTestRule.setContent {
            DeckListContent(
                decks = listOf(Deck(1, "Name", "Long")),
                showEmptyDeckDialogState = true,
                quickStartEnabled = false,
                onDeckSelected = {},
                onStartSessionClick = {},
                onDismissEmptyDeckDialog = { dismissCalled = true },
                onTriggerEmptyDeckDialog = {}
            )
        }

        // Verify Dialog Title
        composeTestRule.onNodeWithText(context.getString(R.string.empty_deck_title))
            .assertIsDisplayed()

        // Click Dismiss Button
        composeTestRule.onNodeWithText(context.getString(R.string.empty_deck_confirm))
            .performClick()

        assert(dismissCalled)
    }

    @Test
    fun testDeckListContent_quickStartDialog_triggersConfirm() {
        var startSessionDeck: Deck? = null
        val mockDeck = Deck(id = 1, name = "Deck 1", longName = "Deck 1 Long", cardCount = 10)

        composeTestRule.setContent {
            DeckListContent(
                decks = listOf(mockDeck),
                showEmptyDeckDialogState = false,
                quickStartEnabled = false,
                onDeckSelected = {},
                onStartSessionClick = { startSessionDeck = it },
                onDismissEmptyDeckDialog = {},
                onTriggerEmptyDeckDialog = {}
            )
        }

        // Trigger the dialog by double clicking
        composeTestRule.onNodeWithText("Deck 1").performTouchInput {
            doubleClick()
        }

        // Verify dialog is visible
        composeTestRule.onNodeWithText(context.getString(R.string.flashcard_start_dialog_title))
            .assertIsDisplayed()

        // Click Confirm
        composeTestRule.onNodeWithText(context.getString(R.string.flashcard_start_dialog_confirm))
            .performClick()

        assert(startSessionDeck?.id == 1)
    }

    @Test
    fun testDeckListContent_quickStartDialog_triggersDismiss() {
        val mockDeck = Deck(id = 1, name = "Deck 1", longName = "Deck 1 Long", cardCount = 10)

        composeTestRule.setContent {
            DeckListContent(
                decks = listOf(mockDeck),
                showEmptyDeckDialogState = false,
                quickStartEnabled = false,
                onDeckSelected = {},
                onStartSessionClick = {},
                onDismissEmptyDeckDialog = {},
                onTriggerEmptyDeckDialog = {}
            )
        }

        // Trigger the dialog by double clicking
        composeTestRule.onNodeWithText("Deck 1").performTouchInput {
            doubleClick()
        }

        // Click Dismiss
        composeTestRule.onNodeWithText(context.getString(R.string.flashcard_start_dialog_dismiss))
            .performClick()

        // Verify dialog is gone
        composeTestRule.onNodeWithText(context.getString(R.string.flashcard_start_dialog_title))
            .assertDoesNotExist()
    }

    @Test
    fun testDeckListContent_doubleClickEmptyDeck_triggersEmptyDeckDialogCallback() {
        var triggerDialogCalled = false
        val mockDecks = listOf(
            Deck(id = 1, name = "Empty Deck", longName = "Empty Deck Long", cardCount = 0)
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

        // Double-click the empty deck
        composeTestRule.onNodeWithText("Empty Deck").performTouchInput {
            doubleClick()
        }

        assert(triggerDialogCalled)
    }

    @Test
    fun testDeckCard_selectionSemantics() {
        val selectedDeck = Deck(id = 1, name = "Selected", longName = "L", isSelected = true)
        val unselectedDeck = Deck(id = 2, name = "Unselected", longName = "L", isSelected = false)

        composeTestRule.setContent {
            Column {
                DeckCard(deck = selectedDeck, onClick = {}, onDoubleClick = {})
                DeckCard(deck = unselectedDeck, onClick = {}, onDoubleClick = {})
            }
        }

        composeTestRule.onNodeWithText("Selected").assertIsSelected()
        composeTestRule.onNodeWithText("Unselected").assertIsNotSelected()
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
