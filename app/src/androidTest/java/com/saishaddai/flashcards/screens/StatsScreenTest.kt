package com.saishaddai.flashcards.screens

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import androidx.test.platform.app.InstrumentationRegistry
import com.saishaddai.flashcards.R
import com.saishaddai.flashcards.model.Deck
import com.saishaddai.flashcards.utils.TestTags
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

class StatsScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val context = InstrumentationRegistry.getInstrumentation().targetContext

    @Test
    fun testStatsScreen_isLoading_showsFullLoader() {
        composeTestRule.setContent {
            StatsContent(
                promoDeck = null,
                weeklyActivity = emptyList(),
                skillMastery = emptyList(),
                cardsReviewed = "0",
                currentStreak = "0",
                studyTime = "0",
                accuracyRate = "0%",
                isLoading = true,
                onBackClicked = {},
                onShareClicked = {},
                onMoreOptionsClicked = {},
                onViewAllSkillsClicked = {},
                onPromoClick = {},
                showSuggestions = true
            )
        }

        composeTestRule.onNodeWithTag(TestTags.FULL_LOADER).assertIsDisplayed()
    }

    @Test
    fun testStatsScreen_loadedState_showsContent() {
        val statsTitle = context.getString(R.string.stats_title)
        
        composeTestRule.setContent {
            StatsContent(
                promoDeck = null,
                weeklyActivity = listOf(10, 20, 30, 40, 50, 60, 70),
                skillMastery = emptyList(),
                cardsReviewed = "100",
                currentStreak = "5",
                studyTime = "2h",
                accuracyRate = "80%",
                isLoading = false,
                onBackClicked = {},
                onShareClicked = {},
                onMoreOptionsClicked = {},
                onViewAllSkillsClicked = {},
                onPromoClick = {},
                showSuggestions = true
            )
        }

        composeTestRule.onNodeWithTag(TestTags.FULL_LOADER).assertDoesNotExist()
        composeTestRule.onNodeWithText(statsTitle).assertIsDisplayed()
    }

    @Test
    fun testStatsScreen_onPromoClick_triggersCallBack() {
        var promoClicked = false

        val mockDeckName = "Test Deck"
        val mockDeck = Deck(id = 1, name = mockDeckName, longName = "Test Deck Long", cardCount = 10)

        composeTestRule.setContent {
            StatsContent(
                promoDeck = mockDeck,
                weeklyActivity = listOf(10, 20, 30, 42, 50, 60, 70),
                skillMastery = emptyList(),
                cardsReviewed = "1,234",
                currentStreak = "7",
                studyTime = "12h 30m",
                accuracyRate = "92%",
                isLoading = false,
                onBackClicked = {},
                onShareClicked = {},
                onMoreOptionsClicked = {},
                onViewAllSkillsClicked = {},
                onPromoClick = { promoClicked = true },
                showSuggestions = true
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
    fun testStatsScreen_weeklyActivityGraph_isDisplayed() {
        composeTestRule.setContent {
            StatsContent(
                promoDeck = null,
                weeklyActivity = listOf(10, 20, 30, 40, 50, 60, 70),
                skillMastery = emptyList(),
                cardsReviewed = "100",
                currentStreak = "5",
                studyTime = "2h",
                accuracyRate = "80%",
                isLoading = false,
                onBackClicked = {},
                onShareClicked = {},
                onMoreOptionsClicked = {},
                onViewAllSkillsClicked = {},
                onPromoClick = {},
                showSuggestions = true
            )
        }

        composeTestRule.onNodeWithTag(TestTags.STATS_WEEKLY_ACTIVITY).assertIsDisplayed()
    }
}