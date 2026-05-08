package com.saishaddai.flashcards.screens

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.saishaddai.flashcards.model.Deck
import com.saishaddai.flashcards.model.Flashcard as FlashcardModel
import com.saishaddai.flashcards.ui.theme.Flashcards2Theme
import androidx.compose.ui.test.onNodeWithTag
import com.saishaddai.flashcards.utils.TestTags
import org.junit.Rule
import org.junit.Test

class FlashcardScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val testDeck = Deck(
        id = 1,
        name = "OOP",
        longName = "Object Oriented Programming"
    )

    @Test
    fun flashcardScreen_initialState_showsQuestionAndHideAnswer() {
        composeTestRule.setContent {
            Flashcards2Theme {
                FlashcardScreen(
                    onCancelSessionClick = {},
                    onFinishedSessionClick = {},
                    deck = testDeck
                )
            }
        }

        // Check if the deck name is displayed in the top bar
        composeTestRule.onNodeWithText("OOP").assertIsDisplayed()

        // Check if the "QUESTION" label is displayed (it's in the Card)
        composeTestRule.onNodeWithText("QUESTION").assertIsDisplayed()

        // Check if the "Show Response" button is displayed
        // Based on strings.xml: <string name="flashcard_button_show_response">Show Response</string>
        composeTestRule.onNodeWithText("Show Response").assertIsDisplayed()

        // The answer label "ANSWER" should NOT be displayed initially
        composeTestRule.onNodeWithText("ANSWER").assertDoesNotExist()
    }

    @Test
    fun flashcardScreen_clickShowResponse_revealsAnswer() {
        composeTestRule.setContent {
            Flashcards2Theme {
                FlashcardScreen(
                    onCancelSessionClick = {},
                    onFinishedSessionClick = {},
                    deck = testDeck
                )
            }
        }

        // Click "Show Response"
        composeTestRule.onNodeWithText("Show Response").performClick()

        // Now the "ANSWER" label should be displayed
        composeTestRule.onNodeWithText("ANSWER").assertIsDisplayed()
    }

    @Test
    fun flashcardScreen_clickFlashcard_revealsAnswer() {
        composeTestRule.setContent {
            Flashcards2Theme {
                FlashcardScreen(
                    onCancelSessionClick = {},
                    onFinishedSessionClick = {},
                    deck = testDeck
                )
            }
        }

        // The answer label "ANSWER" should NOT be displayed initially
        composeTestRule.onNodeWithText("ANSWER").assertDoesNotExist()

        // Click on the Flashcard (Question Card)
        composeTestRule.onNodeWithText("QUESTION").performClick()

        // Now the "ANSWER" label should be displayed
        composeTestRule.onNodeWithText("ANSWER").assertIsDisplayed()
    }

    @Test
    fun flashcardScreen_clickCancelSession_showsConfirmationDialog() {
        composeTestRule.setContent {
            Flashcards2Theme {
                FlashcardScreen(
                    onCancelSessionClick = {},
                    onFinishedSessionClick = {},
                    deck = testDeck
                )
            }
        }

        // Click "CANCEL SESSION"
        // Based on strings.xml: <string name="flashcard_button_cancel_session">CANCEL SESSION</string>
        composeTestRule.onNodeWithText("CANCEL SESSION").performClick()

        // Check if the dialog title is displayed
        // Based on strings.xml: <string name="flashcard_cancel_dialog_title">Cancel Session?</string>
        composeTestRule.onNodeWithText("Cancel Session?").assertIsDisplayed()
        
        // Check if 'confirm' and 'dismiss' buttons are present
        // Based on strings.xml: 
        // <string name="flashcard_cancel_dialog_confirm">Yes, Cancel</string>
        // <string name="flashcard_cancel_dialog_dismiss">No, Continue</string>
        composeTestRule.onNodeWithText("Yes, Cancel").assertIsDisplayed()
        composeTestRule.onNodeWithText("No, Continue").assertIsDisplayed()
    }

    @Test
    fun flashcardScreen_isLoading_showsFullLoader() {
        composeTestRule.setContent {
            Flashcards2Theme {
                FlashcardContent(
                    deck = testDeck,
                    flashcards = emptyList(),
                    showAnswer = false,
                    isFinished = false,
                    isLoading = true,
                    onShowResponseClicked = {},
                    onPageChanged = {},
                    onFinishSession = {},
                    onCancelSessionClick = {},
                    onFinishedSessionClick = {}
                )
            }
        }

        composeTestRule.onNodeWithTag(TestTags.FULL_LOADER).assertIsDisplayed()
    }

    @Test
    fun flashcardScreen_confirmCancelSession_callsCallback() {
        var cancelClicked = false
        composeTestRule.setContent {
            Flashcards2Theme {
                FlashcardContent(
                    deck = testDeck,
                    flashcards = listOf(
                        FlashcardModel(1, 1, "Q1", "A1"),
                        FlashcardModel(1, 1, "Q2", "A2")
                    ),
                    showAnswer = false,
                    isFinished = false,
                    isLoading = false,
                    onShowResponseClicked = {},
                    onPageChanged = {},
                    onFinishSession = {},
                    onCancelSessionClick = { cancelClicked = true },
                    onFinishedSessionClick = {}
                )
            }
        }

        composeTestRule.onNodeWithText("CANCEL SESSION").performClick()
        composeTestRule.onNodeWithText("Yes, Cancel").performClick()

        assert(cancelClicked)
    }

    @Test
    fun flashcardScreen_lastPage_showsFinishButton() {
        var finishClicked = false
        composeTestRule.setContent {
            Flashcards2Theme {
                FlashcardContent(
                    deck = testDeck,
                    flashcards = listOf(FlashcardModel(1, 1, "Q", "A")),
                    showAnswer = true,
                    isFinished = false,
                    isLoading = false,
                    onShowResponseClicked = {},
                    onPageChanged = {},
                    onFinishSession = { finishClicked = true },
                    onCancelSessionClick = {},
                    onFinishedSessionClick = {}
                )
            }
        }

        // On the last page, "FINISH SESSION" should be visible
        composeTestRule.onNodeWithText("FINISH SESSION").assertIsDisplayed()
        composeTestRule.onNodeWithText("FINISH SESSION").performClick()
        
        assert(finishClicked)
    }

    @Test
    fun flashcardScreen_isFinished_callsFinishedCallback() {
        var finishedSessionCalled = false
        composeTestRule.setContent {
            Flashcards2Theme {
                FlashcardContent(
                    deck = testDeck,
                    flashcards = emptyList(),
                    showAnswer = false,
                    isFinished = true,
                    isLoading = false,
                    onShowResponseClicked = {},
                    onPageChanged = {},
                    onFinishSession = {},
                    onCancelSessionClick = {},
                    onFinishedSessionClick = { finishedSessionCalled = true }
                )
            }
        }

        assert(finishedSessionCalled)
    }

    @Test
    fun flashcardScreen_showAnswerTrue_displaysAnswerInitially() {
        composeTestRule.setContent {
            Flashcards2Theme {
                FlashcardContent(
                    deck = testDeck,
                    flashcards = listOf(FlashcardModel(1, 1, "Q", "A")),
                    showAnswer = true,
                    isFinished = false,
                    isLoading = false,
                    onShowResponseClicked = {},
                    onPageChanged = {},
                    onFinishSession = {},
                    onCancelSessionClick = {},
                    onFinishedSessionClick = {}
                )
            }
        }

        // Now the "ANSWER" label should be displayed immediately
        composeTestRule.onNodeWithText("ANSWER").assertIsDisplayed()
    }
}
