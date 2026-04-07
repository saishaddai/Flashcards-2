package com.saishaddai.flashcards.screens

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.saishaddai.flashcards.model.Deck
import com.saishaddai.flashcards.repository.DeckRepository
import com.saishaddai.flashcards.viewmodel.DecksViewModel
import org.junit.Rule
import org.junit.Test

class DeckListScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()


    @Test
    fun testDeckListScreen_initialState_showDeckListInfo() {
        composeTestRule.setContent {
            DeckListScreen(onStartSessionClick = {})
        }

        composeTestRule.onNodeWithText("WELCOME BACK").assertIsDisplayed()
        composeTestRule.onNodeWithText("What are you learning today?").assertIsDisplayed()
        composeTestRule.onNodeWithText("Start Session").assertIsDisplayed()
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

        val viewModel = DecksViewModel(fakeRepository)

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
            Deck(id = 1, name = "Empty Deck", longName = "Empty Deck Long", cardCount = 0)
        )

        val fakeRepository = object : DeckRepository<Deck> {
            override suspend fun getData(): List<Deck> = mockDecks
        }

        val viewModel = DecksViewModel(fakeRepository)

        composeTestRule.setContent {
            DeckListScreen(viewModel = viewModel, onStartSessionClick = {})
        }

        composeTestRule.onNodeWithText("Empty Deck").performClick()
        composeTestRule.onNodeWithText("Start Session").assertIsDisplayed().performClick()

        //check if ot opened a Dialog saying there's no flashcards to show
        composeTestRule.onNodeWithText("Got it").assertIsDisplayed()
            .performClick().assertDoesNotExist()

    }

    @Test
    fun testDeckListScreen_clickMockDeck_triggersCallBack() {
        var onStartSessionClick = false
        val mockDecks = listOf(
            Deck(id = 1, name = "Mock Deck", longName = "Mock Deck Long", cardCount = 1)
        )

        val fakeRepository = object : DeckRepository<Deck> {
            override suspend fun getData(): List<Deck> = mockDecks
        }

        val viewModel = DecksViewModel(fakeRepository)

        composeTestRule.setContent {
            DeckListScreen(viewModel = viewModel, onStartSessionClick = { onStartSessionClick = true })
        }

        composeTestRule.onNodeWithText("Mock Deck").performClick()
        composeTestRule.onNodeWithText("Start Session").assertIsDisplayed().performClick()

        //check if onStartSessionCLick was called
        composeTestRule.waitUntil(5000) { onStartSessionClick }

    }


}
