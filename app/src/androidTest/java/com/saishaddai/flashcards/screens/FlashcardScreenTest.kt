package com.saishaddai.flashcards.screens

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTouchInput
import androidx.compose.ui.test.swipeLeft
import com.saishaddai.flashcards.model.Deck
import com.saishaddai.flashcards.ui.theme.Flashcards2Theme
import com.saishaddai.flashcards.utils.TestTags
import com.saishaddai.flashcards.utils.UiState
import org.junit.Rule
import org.junit.Test
import com.saishaddai.flashcards.model.Flashcard as FlashcardModel

class FlashcardScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val testDeck = Deck(
        id = 1,
        name = "OOP",
        longName = "Object Oriented Programming"
    )

    @Test
    fun flashcardScreen_loadingState_showsLoader() {
        composeTestRule.setContent {
            Flashcards2Theme {
                FlashcardScreen(
                    uiState = UiState.Loading,
                    deck = testDeck,
                    onShowResponseClicked = {},
                    onPageChanged = {},
                    onFinishSession = {},
                    onCancelSessionClick = {},
                    onFinishedSessionClick = {},
                    onStartTimer = {},
                    onPauseTimer = {},
                    onRetry = {}
                )
            }
        }

        composeTestRule.onNodeWithTag(TestTags.FULL_LOADER).assertIsDisplayed()
        composeTestRule.onNodeWithText("Loading…").assertIsDisplayed()
    }

    @Test
    fun flashcardScreen_errorState_showsErrorViewAndRetries() {
        var retryClicked = false
        composeTestRule.setContent {
            Flashcards2Theme {
                FlashcardScreen(
                    uiState = UiState.Error("Error Message"),
                    deck = testDeck,
                    onShowResponseClicked = {},
                    onPageChanged = {},
                    onFinishSession = {},
                    onCancelSessionClick = {},
                    onFinishedSessionClick = {},
                    onStartTimer = {},
                    onPauseTimer = {},
                    onRetry = { retryClicked = true }
                )
            }
        }

        composeTestRule.onNodeWithText("Oops!").assertIsDisplayed()
        composeTestRule.onNodeWithText("Error Message").assertIsDisplayed()
        composeTestRule.onNodeWithText("Retry").performClick()

        assert(retryClicked)
    }

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
                    flashcards = listOf(
                        FlashcardModel(1, 1, "Q1", "A1"),
                        FlashcardModel(1, 2, "Q2", "A2")
                    ),
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
                        FlashcardModel(1, 1, "Q1", "A1"),
                        FlashcardModel(1, 2, "Q2", "A2")
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
    fun flashcardContent_dismissCancelDialog_hidesDialog() {
        composeTestRule.setContent {
            Flashcards2Theme {
                FlashcardContent(
                    deck = testDeck,
                    flashcards = listOf(
                        FlashcardModel(1, 1, "Q1", "A1"),
                        FlashcardModel(1, 2, "Q2", "A2")
                    ),
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

        // Trigger dialog
        composeTestRule.onNodeWithText("CANCEL SESSION").performClick()
        composeTestRule.onNodeWithText("Cancel Session?").assertIsDisplayed()

        // Dismiss dialog
        composeTestRule.onNodeWithText("No, Continue").performClick()

        // Verify dialog is gone
        composeTestRule.onNodeWithText("Cancel Session?").assertDoesNotExist()
    }

    @Test
    fun flashcardContent_showAnswerTrue_showsAnswer() {
        val answerText = "This is the correct answer"
        composeTestRule.setContent {
            Flashcards2Theme {
                FlashcardContent(
                    deck = testDeck,
                    flashcards = listOf(FlashcardModel(1, 1, "Question?", answerText)),
                    showAnswer = true,
                    isFinished = false,
                    onShowResponseClicked = {},
                    onPageChanged = { _ -> },
                    onFinishSession = { _ -> },
                    onCancelSessionClick = {},
                    onFinishedSessionClick = {}
                )
            }
        }

        // Check if the answer label and text are visible
        composeTestRule.onNodeWithText("ANSWER").assertIsDisplayed()
        composeTestRule.onNodeWithText(answerText).assertIsDisplayed()
    }

    @Test
    fun flashcardContent_progressIndicator_showsCorrectProgress() {
        composeTestRule.setContent {
            Flashcards2Theme {
                FlashcardContent(
                    deck = testDeck,
                    flashcards = listOf(
                        FlashcardModel(1, 1, "Q1", "A1"),
                        FlashcardModel(1, 2, "Q2", "A2")
                    ),
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

        // On page 0 (initial), it should show "1/2" (current is currentPage + 1)
        composeTestRule.onNodeWithText("1/2").assertIsDisplayed()
        // Progress percentage for 1/2 is 50%
        composeTestRule.onNodeWithText("50% Complete").assertIsDisplayed()
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
    fun flashcardContent_swipeToNextCard_callsPageChanged() {
        var pageChangedCalledWith: Int? = null
        composeTestRule.setContent {
            Flashcards2Theme {
                FlashcardContent(
                    deck = testDeck,
                    flashcards = listOf(
                        FlashcardModel(1, 1, "Q1", "A1"),
                        FlashcardModel(1, 2, "Q2", "A2")
                    ),
                    showAnswer = false,
                    isFinished = false,
                    onShowResponseClicked = {},
                    onPageChanged = { page -> pageChangedCalledWith = page },
                    onFinishSession = { _ -> },
                    onCancelSessionClick = {},
                    onFinishedSessionClick = {}
                )
            }
        }

        // Swipe left to go to the next card
        composeTestRule.onNodeWithText("Q1").performTouchInput {
            swipeLeft()
        }

        composeTestRule.waitForIdle()

        // Page index should be 1
        assert(pageChangedCalledWith == 1)
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
