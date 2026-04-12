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

class StatsScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val context = InstrumentationRegistry.getInstrumentation().targetContext

    @Test
    fun testStatsScreen_initState_checkElementsDisplayed() {
        composeTestRule.setContent {
            StatsScreen()
        }
    }

    @Test
    fun testStatsScreen_onScrollableContent_checkElementsDisplayed() {
    }

    @Test
    fun testStatsScreen_onBackClick_checkNavigationCalled() {
    }

    @Test
    fun testStatsScreen_onPromoClick_checkNavigationCalled() {
    }

    @Test
    fun testStatsScreen_onSeeAllClick_showsAllDecksInfoCards() {
    }

    @Test
    fun testStatsScreen_onShareClick_triggersCallBack() {
    }

    @Test
    fun testStatsScreen_onPromoClick_triggersCallBack() {
        var promoClicked = false

        val mockDeckName = "Test Deck"
        val mockDeck = Deck(id = 1, name = mockDeckName, longName = "Test Deck Long", cardCount = 10)
        val fakeRepository = object : DeckRepository<Deck> {
            override suspend fun getData(): List<Deck> = listOf(mockDeck)

        }
        val viewModel = DecksViewModel(fakeRepository)


        composeTestRule.setContent {
            StatsScreen(
                decksViewModel = viewModel,
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

}