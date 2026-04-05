package com.saishaddai.flashcards.screens

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.saishaddai.flashcards.model.Deck
import com.saishaddai.flashcards.ui.theme.Flashcards2Theme
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

class FinishSessionScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val testDeck = Deck(
        id = 1,
        name = "OOP",
        longName = "Object Oriented Programming"
    )

    @Test
    fun finishSessionScreen_initialState_showsSummaryInfo() {
        composeTestRule.setContent {
            Flashcards2Theme {
                FinishSessionScreen(
                    deck = testDeck,
                    onFinishSession = {},
                    onShareSummary = {}
                )
            }
        }

        // Verify Top Bar Title
        composeTestRule.onNodeWithText("SESSION SUMMARY").assertIsDisplayed()

        // Verify "All Done!" message
        composeTestRule.onNodeWithText("All Done!").assertIsDisplayed()

        // Verify the success message with deck name
        // stringResource(R.string.finish_great_job, deck.name) -> "Great job on your 'OOP' prep today."
        composeTestRule.onNodeWithText("Great job on your 'OOP' prep today.").assertIsDisplayed()

        // Verify Info Cards labels
        composeTestRule.onNodeWithText("REVIEWED").assertIsDisplayed()
        composeTestRule.onNodeWithText("DURATION").assertIsDisplayed()

        // Verify the "Back to Decks" button is visible
        composeTestRule.onNodeWithText("BACK TO DECKS").assertIsDisplayed()
    }

    @Test
    fun finishSessionScreen_clickBackToDecks_triggersCallback() {
        var finishSessionCalled = false
        composeTestRule.setContent {
            Flashcards2Theme {
                FinishSessionScreen(
                    deck = testDeck,
                    onFinishSession = { finishSessionCalled = true },
                    onShareSummary = {}
                )
            }
        }

        // Click "Back to Decks"
        composeTestRule.onNodeWithText("BACK TO DECKS").performClick()

        // Verify callback was triggered
        assertTrue(finishSessionCalled)
    }

    @Test
    fun finishSessionScreen_clickTopCheckIcon_triggersCallback() {
        var finishSessionCalled = false
        composeTestRule.setContent {
            Flashcards2Theme {
                FinishSessionScreen(
                    deck = testDeck,
                    onFinishSession = { finishSessionCalled = true },
                    onShareSummary = {}
                )
            }
        }

        // Click the check icon in the navigation bar
        // contentDescription: R.string.finish_nav_icon_content_desc -> "Close"
        composeTestRule.onNodeWithContentDescription("Close").performClick()

        // Verify callback was triggered
        assertTrue(finishSessionCalled)
    }

    @Test
    fun finishSessionScreen_clickShareIcon_triggersCallback() {
        var shareSummaryCalled = false
        composeTestRule.setContent {
            Flashcards2Theme {
                FinishSessionScreen(
                    deck = testDeck,
                    onFinishSession = {},
                    onShareSummary = { shareSummaryCalled = true }
                )
            }
        }

        // Click the share icon
        // contentDescription: R.string.finish_action_share_content_desc -> "Share"
        composeTestRule.onNodeWithContentDescription("Share").performClick()

        // Verify callback was triggered
        assertTrue(shareSummaryCalled)
    }
}
