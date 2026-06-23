package com.saishaddai.flashcards.screens

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
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
    fun flashcardContent_initialState_showsQuestionAndHideAnswer() {
        composeTestRule.setContent {
            Flashcards2Theme {
                FlashcardContent(
                    deck = testDeck,
                    flashcards = listOf(FlashcardModel(1, 1, "What is OOP?", "Answer")),
                    showAnswer = false,
                    isFinished = false,
                    onShowResponseClicked = {},
                    onPageChanged = { _ -> },
                    onFinishSession = { _ -> },
                    onCancelSessionClick = {},
                    onFinishedSessionClick = {}
                )
            }
        }

        // Check if the deck name is displayed in the top bar
        composeTestRule.onNodeWithText("OOP").assertIsDisplayed()

        // Check if the "QUESTION" label is displayed (it's in the Card)
        composeTestRule.onNodeWithText("QUESTION").assertIsDisplayed()

        // Check if the "Show Response" button is displayed
        composeTestRule.onNodeWithText("Show Response").assertIsDisplayed()

        // The answer label "ANSWER" should NOT be displayed initially
        composeTestRule.onNodeWithText("ANSWER").assertDoesNotExist()
    }

    @Test
    fun flashcardContent_clickShowResponse_callsCallback() {
        var showResponseClicked = false
        composeTestRule.setContent {
            Flashcards2Theme {
                FlashcardContent(
                    deck = testDeck,
                    flashcards = listOf(FlashcardModel(1, 1, "Q", "A")),
                    showAnswer = false,
                    isFinished = false,
                    onShowResponseClicked = { showResponseClicked = true },
                    onPageChanged = { _ -> },
                    onFinishSession = { _ -> },
                    onCancelSessionClick = {},
                    onFinishedSessionClick = {}
                )
            }
        }

        // Click "Show Response"
        composeTestRule.onNodeWithText("Show Response").performClick()

        assert(showResponseClicked)
    }

    @Test
    fun flashcardContent_clickFlashcard_callsCallback() {
        var showResponseClicked = false
        composeTestRule.setContent {
            Flashcards2Theme {
                FlashcardContent(
                    deck = testDeck,
                    flashcards = listOf(FlashcardModel(1, 1, "Q", "A")),
                    showAnswer = false,
                    isFinished = false,
                    onShowResponseClicked = { showResponseClicked = true },
                    onPageChanged = { _ -> },
                    onFinishSession = { _ -> },
                    onCancelSessionClick = {},
                    onFinishedSessionClick = {}
                )
            }
        }

        // Click on the Flashcard (Question Card)
        composeTestRule.onNodeWithText("QUESTION").performClick()

        assert(showResponseClicked)
    }

    @Test
    fun flashcardContent_clickCancelSession_showsConfirmationDialog() {
        composeTestRule.setContent {
            Flashcards2Theme {
                FlashcardContent(
                    deck = testDeck,
                    flashcards = listOf(FlashcardModel(1, 1, "Q", "A")),
                    showAnswer = false,
                    isFinished = false,
                    onShowResponseClicked = {},
                    onPageChanged = { _ -> },
                    onFinishSession = { _ -> },
                    onCancelSessionClick = {},
                    onFinishedSessionClick = {}
                )
            }
        }

        // Click "CANCEL SESSION"
        composeTestRule.onNodeWithText("CANCEL SESSION").performClick()

        // Check if the dialog title is displayed
        composeTestRule.onNodeWithText("Cancel Session?").assertIsDisplayed()
        
        composeTestRule.onNodeWithText("Yes, Cancel").assertIsDisplayed()
        composeTestRule.onNodeWithText("No, Continue").assertIsDisplayed()
    }

    @Test
    fun flashcardContent_confirmCancelSession_callsCallback() {
        var cancelClicked = false
        composeTestRule.setContent {
            Flashcards2Theme {
                FlashcardContent(
                    deck = testDeck,
                    flashcards = listOf(
                        FlashcardModel(1, 1, "Q1", "A1")
                    ),
                    showAnswer = false,
                    isFinished = false,
                    onShowResponseClicked = {},
                    onPageChanged = { _ -> },
                    onFinishSession = { _ -> },
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
    fun flashcardContent_lastPage_showsFinishButton() {
        var finishClicked = false
        composeTestRule.setContent {
            Flashcards2Theme {
                FlashcardContent(
                    deck = testDeck,
                    flashcards = listOf(FlashcardModel(1, 1, "Q", "A")),
                    showAnswer = true,
                    isFinished = false,
                    onShowResponseClicked = {},
                    onPageChanged = { _ -> },
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
    fun flashcardContent_isFinished_callsFinishedCallback() {
        var finishedSessionCalled = false
        composeTestRule.setContent {
            Flashcards2Theme {
                FlashcardContent(
                    deck = testDeck,
                    flashcards = emptyList(),
                    showAnswer = false,
                    isFinished = true,
                    onShowResponseClicked = {},
                    onPageChanged = { _ -> },
                    onFinishSession = { _ -> },
                    onCancelSessionClick = {},
                    onFinishedSessionClick = { finishedSessionCalled = true }
                )
            }
        }

        assert(finishedSessionCalled)
    }
}
