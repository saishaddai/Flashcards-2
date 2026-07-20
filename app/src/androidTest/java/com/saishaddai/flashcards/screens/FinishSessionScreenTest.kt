package com.saishaddai.flashcards.screens

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import com.saishaddai.flashcards.model.Deck
import com.saishaddai.flashcards.ui.theme.Flashcards2Theme
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
    fun finishSessionContent_initialState_showsSummaryInfo() {
        composeTestRule.setContent {
            Flashcards2Theme {
                FinishSessionContent(
                    deck = testDeck,
                    cardsReviewed = 20,
                    startTime = 0L,
                    endTime = 1000L * 60 * 12, // 12 mins
                    totalTimeMillis = 1000L * 60 * 12,
                    sessionResult = null,
                    onBackToDecksClicked = {},
                    onShareSummary = {}
                )
            }
        }

        // Verify Top Bar Title
        composeTestRule.onNodeWithText("SESSION SUMMARY").assertIsDisplayed()

        // Verify "All Done!" message
        composeTestRule.onNodeWithText("All Done!").assertIsDisplayed()

        // Verify the success message with deck name
        composeTestRule.onNodeWithText("Great job on your 'OOP' prep today.").assertIsDisplayed()

        // Verify Info Cards labels
        composeTestRule.onNodeWithText("REVIEWED").assertIsDisplayed()
        composeTestRule.onNodeWithText("DURATION").assertIsDisplayed()

        // Verify the "Back to Decks" button is visible
        composeTestRule.onNodeWithText("Back to Decks", ignoreCase = true).performScrollTo().assertIsDisplayed()
    }

    @Test
    fun finishSessionContent_clickBackToDecks_callsCallback() {
        var backClicked = false
        composeTestRule.setContent {
            Flashcards2Theme {
                FinishSessionContent(
                    deck = testDeck,
                    cardsReviewed = 10,
                    startTime = 0L,
                    endTime = 1000L,
                    totalTimeMillis = 1000L,
                    sessionResult = null,
                    onBackToDecksClicked = { backClicked = true },
                    onShareSummary = {}
                )
            }
        }

        // Click "Back to Decks" after scrolling
        composeTestRule.onNodeWithText("Back to Decks", ignoreCase = true)
            .performScrollTo()
            .performClick()

        assert(backClicked)
    }

    @Test
    fun finishSessionContent_clickTopCheckIcon_callsCallback() {
        var backClicked = false
        composeTestRule.setContent {
            Flashcards2Theme {
                FinishSessionContent(
                    deck = testDeck,
                    cardsReviewed = 10,
                    startTime = 0L,
                    endTime = 1000L,
                    totalTimeMillis = 1000L,
                    sessionResult = null,
                    onBackToDecksClicked = { backClicked = true },
                    onShareSummary = {}
                )
            }
        }

        composeTestRule.onNodeWithContentDescription("Close").performClick()

        assert(backClicked)
    }

    @Test
    fun finishSessionContent_clickShareIcon_callsCallback() {
        var shareSummaryCalled = false
        composeTestRule.setContent {
            Flashcards2Theme {
                FinishSessionContent(
                    deck = testDeck,
                    cardsReviewed = 10,
                    startTime = 0L,
                    endTime = 1000L,
                    totalTimeMillis = 1000L,
                    sessionResult = null,
                    onBackToDecksClicked = {},
                    onShareSummary = { shareSummaryCalled = true }
                )
            }
        }

        // Click the share icon
        composeTestRule.onNodeWithContentDescription("Share").performClick()

        assert(shareSummaryCalled)
    }
}
