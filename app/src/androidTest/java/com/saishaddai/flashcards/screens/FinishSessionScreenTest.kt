package com.saishaddai.flashcards.screens

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import com.saishaddai.flashcards.model.Deck
import com.saishaddai.flashcards.ui.theme.Flashcards2Theme
import com.saishaddai.flashcards.utils.TestTags
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
        composeTestRule.onNodeWithText("Great job on your 'OOP' prep today.").assertIsDisplayed()

        // Verify Info Cards labels
        composeTestRule.onNodeWithText("REVIEWED").assertIsDisplayed()
        composeTestRule.onNodeWithText("DURATION").assertIsDisplayed()

        // Verify the "Back to Decks" button is visible
        composeTestRule.onNodeWithText("Back to Decks", ignoreCase = true).performScrollTo().assertIsDisplayed()
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

        // Click "Back to Decks" after scrolling
        composeTestRule.onNodeWithText("Back to Decks", ignoreCase = true)
            .performScrollTo()
            .performClick()

        // Wait for the LaunchedEffect to trigger the callback via ViewModel state change
        composeTestRule.waitUntil(5000) {
            finishSessionCalled
        }
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

        // Click the check icon in the navigation bar (Close content description)
        composeTestRule.onNodeWithContentDescription("Close").performClick()

        // Wait for the LaunchedEffect to trigger the callback
        composeTestRule.waitUntil(5000) {
            finishSessionCalled
        }
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
        composeTestRule.onNodeWithContentDescription("Share").performClick()

        // Verify callback was triggered
        composeTestRule.runOnIdle {
            assert(shareSummaryCalled)
        }
    }

    @Test
    fun finishSessionScreen_isLoading_showsFullLoader() {
        composeTestRule.setContent {
            Flashcards2Theme {
                FinishSessionContent(
                    deck = testDeck,
                    isLoading = true,
                    onBackToDecksClicked = {},
                    onShareSummary = {}
                )
            }
        }

        composeTestRule.onNodeWithTag(TestTags.FULL_LOADER).assertIsDisplayed()
    }
}
