package com.saishaddai.flashcards.screens

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import androidx.test.platform.app.InstrumentationRegistry
import com.saishaddai.flashcards.R
import com.saishaddai.flashcards.model.Deck
import com.saishaddai.flashcards.repository.DeckRepository
import com.saishaddai.flashcards.viewmodel.DecksViewModel
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

class InstructionsScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val context = InstrumentationRegistry.getInstrumentation().targetContext

    @Test
    fun instructionsScreen_initialState_showsInstructions() {
        composeTestRule.setContent {
            InstructionsScreen()
        }

        composeTestRule.onNodeWithText(context.getString(R.string.instructions_guide_label)).assertIsDisplayed()
        composeTestRule.onNodeWithText(context.getString(R.string.instructions_title)).assertIsDisplayed()
        
        val descPart = context.getString(R.string.instructions_description).take(20)
        composeTestRule.onNodeWithText(descPart, substring = true).assertIsDisplayed()
        
        composeTestRule.onNodeWithText(context.getString(R.string.instructions_daily_consistency_title)).assertIsDisplayed()
        composeTestRule.onNodeWithText(context.getString(R.string.instructions_offline_first_title)).assertIsDisplayed()
        composeTestRule.onNodeWithText(context.getString(R.string.instructions_sessions_title)).assertIsDisplayed()
    }

    @Test
    fun instructionsScreen_clickPromo_triggersCallback() {
        var promoClicked = false
        val mockDeckName = "Test Deck"
        val mockDeck = Deck(id = 1, name = mockDeckName, longName = "Test Deck Long", cardCount = 10)
        
        val fakeRepository = object : DeckRepository<Deck> {
            override suspend fun getData(): List<Deck> = listOf(mockDeck)
        }
        val application = context.applicationContext as android.app.Application
        val viewModel = DecksViewModel(application, fakeRepository)

        composeTestRule.setContent {
            InstructionsScreen(
                viewModel = viewModel,
                onPromoClick = { promoClicked = true }
            )
        }

        val startNowText = context.getString(R.string.promo_widget_confirm)

        // Wait for the asynchronous data to load and the PromoWidget to appear
        composeTestRule.waitUntil(5000) {
            composeTestRule.onAllNodesWithText(startNowText).fetchSemanticsNodes().isNotEmpty()
        }

        // SCROLL to the PromoWidget first. If it's off-screen, assertions will fail.
        composeTestRule.onNodeWithText(startNowText).performScrollTo()

        // 1. Check title (using substring to ignore the \n newline)
        val promoTitlePart = context.getString(R.string.promo_widget_title).take(10)
        composeTestRule.onNodeWithText(promoTitlePart, substring = true).assertIsDisplayed()
        
        // 2. Check message containing the deck name. 
        // We match the deck name as a substring of the formatted message.
        composeTestRule.onNodeWithText(mockDeckName, substring = true).assertIsDisplayed()
        
        // 3. Click the button
        composeTestRule.onNodeWithText(startNowText).assertIsDisplayed().assertIsEnabled().performClick()
        
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
        val application = context.applicationContext as android.app.Application
        val viewModel = DecksViewModel(application, fakeRepository)

        composeTestRule.setContent {
            InstructionsScreen(viewModel = viewModel)
        }

        val startNowText = context.getString(R.string.promo_widget_confirm)

        // Wait for content to load
        composeTestRule.waitUntil(5000) {
            composeTestRule.onAllNodesWithText(startNowText).fetchSemanticsNodes().isNotEmpty()
        }

        // The button is at the bottom, so we must scroll to it
        composeTestRule.onNodeWithText(startNowText).performScrollTo().assertIsDisplayed()
    }
}
