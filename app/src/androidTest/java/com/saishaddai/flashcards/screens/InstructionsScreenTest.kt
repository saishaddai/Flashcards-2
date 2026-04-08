package com.saishaddai.flashcards.screens

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import com.saishaddai.flashcards.model.Deck
import com.saishaddai.flashcards.repository.DeckRepository
import com.saishaddai.flashcards.viewmodel.DecksViewModel
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

class InstructionsScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun instructionsScreen_initialState_showsInstructions() {
        composeTestRule.setContent {
            InstructionsScreen()
        }

        composeTestRule.onNodeWithText("GUIDE").assertIsDisplayed()
        composeTestRule.onNodeWithText("How it Works").assertIsDisplayed()
        composeTestRule.onNodeWithText("Master Android development concepts", substring = true).assertIsDisplayed()
        
        composeTestRule.onNodeWithText("Daily Consistency").assertIsDisplayed()
        composeTestRule.onNodeWithText("Build long-term retention", substring = true).assertIsDisplayed()
        
        composeTestRule.onNodeWithText("Offline First").assertIsDisplayed()
        composeTestRule.onNodeWithText("Study anywhere, anytime.", substring = true).assertIsDisplayed()
        
        composeTestRule.onNodeWithText("Learning Sessions").assertIsDisplayed()
        composeTestRule.onNodeWithText("Each session presents 20 random flashcards", substring = true).assertIsDisplayed()
    }

    @Test
    fun instructionsScreen_clickPromo_triggersCallback() {
        var promoClicked = false
        val mockDeck = Deck(id = 1, name = "Test Deck", longName = "Test Deck Long", cardCount = 10)
        
        val fakeRepository = object : DeckRepository<Deck> {
            override suspend fun getData(): List<Deck> = listOf(mockDeck)
        }
        val viewModel = DecksViewModel(fakeRepository)

        composeTestRule.setContent {
            InstructionsScreen(
                viewModel = viewModel,
                onPromoClick = { promoClicked = true }
            )
        }

        // Wait for the asynchronous data to load and the PromoWidget to appear
        composeTestRule.waitUntil(5000) {
            composeTestRule.onAllNodesWithText("START NOW").fetchSemanticsNodes().isNotEmpty()
        }

        // 1. Check title (using substring to ignore \n)
        composeTestRule.onNodeWithText("Ready for a", substring = true).assertIsDisplayed()
        
        // 2. Check message containing the deck name
        composeTestRule.onNodeWithText("\'Test Deck\'", substring = true).assertIsDisplayed()
        
        // 3. Click the button
        composeTestRule.onNodeWithText("START NOW").assertIsDisplayed().assertIsEnabled().performClick()
        
        assertTrue("Promo click lambda should have been triggered", promoClicked)
    }
    
    @Test
    fun instructionScreen_checkScrollingCoverEntireScreen() {
        val mockDecks = List(10) { i -> 
            Deck(id = i, name = "Deck $i", longName = "Deck $i Long", cardCount = 10)
        }
        val fakeRepository = object : DeckRepository<Deck> {
            override suspend fun getData(): List<Deck> = mockDecks
        }
        val viewModel = DecksViewModel(fakeRepository)

        composeTestRule.setContent {
            InstructionsScreen(viewModel = viewModel)
        }

        // Wait for content to load
        composeTestRule.waitUntil(5000) {
            composeTestRule.onAllNodesWithText("START NOW").fetchSemanticsNodes().isNotEmpty()
        }

        // The button is at the bottom, so we must scroll to it
        composeTestRule.onNodeWithText("START NOW").performScrollTo().assertIsDisplayed()
    }
}
