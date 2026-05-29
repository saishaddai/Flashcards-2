package com.saishaddai.flashcards.screens

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.test.SemanticsMatcher
import androidx.compose.ui.test.assert
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createComposeRule
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

    private fun hasColor(color: Color): SemanticsMatcher =
        SemanticsMatcher.expectValue(ColorKey, color)

    @Test
    fun testStatsScreen_isLoading_showsFullLoader() {
        composeTestRule.setContent {
            StatsContent(
                promoDeck = null,
                weeklyActivity = emptyList(),
                skillMastery = emptyList(),
                flashcardsViewed = "0",
                currentStreak = "0",
                studyTime = "0",
                masteredDecks = "0",
                weeklyComparison = 0,
                isLoading = true,
                onViewAllSkillsClicked = {},
                onPromoClick = {},
                showSuggestions = true,
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
                flashcardsViewed = "100",
                currentStreak = "5",
                studyTime = "2h",
                masteredDecks = "10",
                weeklyComparison = 0,
                isLoading = false,
                onViewAllSkillsClicked = {},
                onPromoClick = {},
                showSuggestions = true,
            )
        }

        composeTestRule.onNodeWithTag(TestTags.FULL_LOADER).assertDoesNotExist()
        composeTestRule.onNodeWithText(statsTitle).assertIsDisplayed()
    }

    @Test
    fun testStatsScreen_weeklyComparison_positiveGrowth() {
        composeTestRule.setContent {
            StatsContent(
                promoDeck = null,
                weeklyActivity = listOf(10, 20, 30, 40, 50, 60, 70),
                skillMastery = emptyList(),
                flashcardsViewed = "100",
                currentStreak = "5",
                studyTime = "2h",
                masteredDecks = "10",
                weeklyComparison = 15,
                isLoading = false,
                onViewAllSkillsClicked = {},
                onPromoClick = {},
                showSuggestions = true,
            )
        }

        // Check number text and color (Green: 0xFF10B981)
        composeTestRule.onNodeWithTag(TestTags.STATS_PROGRESS_NUMBER)
            .assertIsDisplayed()
            .assert(hasText("+15%"))
            .assert(hasColor(Color(0xFF10B981)))

        // Check icon presence and color
        composeTestRule.onNodeWithTag(TestTags.STATS_PROGRESS_ICON)
            .assertIsDisplayed()
            .assert(hasColor(Color(0xFF10B981)))
    }

    @Test
    fun testStatsScreen_weeklyComparison_negativeGrowth() {
        composeTestRule.setContent {
            StatsContent(
                promoDeck = null,
                weeklyActivity = listOf(10, 20, 30, 40, 50, 60, 70),
                skillMastery = emptyList(),
                flashcardsViewed = "100",
                currentStreak = "5",
                studyTime = "2h",
                masteredDecks = "10",
                weeklyComparison = -8,
                isLoading = false,
                onViewAllSkillsClicked = {},
                onPromoClick = {},
                showSuggestions = true,
            )
        }

        // Check number text and color (Red: 0xFFEF4444)
        composeTestRule.onNodeWithTag(TestTags.STATS_PROGRESS_NUMBER)
            .assertIsDisplayed()
            .assert(hasText("-8%"))
            .assert(hasColor(Color(0xFFEF4444)))

        // Check icon presence and color
        composeTestRule.onNodeWithTag(TestTags.STATS_PROGRESS_ICON)
            .assertIsDisplayed()
            .assert(hasColor(Color(0xFFEF4444)))
    }

    @Test
    fun testStatsScreen_weeklyComparison_zeroGrowth() {
        composeTestRule.setContent {
            StatsContent(
                promoDeck = null,
                weeklyActivity = listOf(10, 20, 30, 40, 50, 60, 70),
                skillMastery = emptyList(),
                flashcardsViewed = "100",
                currentStreak = "5",
                studyTime = "2h",
                masteredDecks = "10",
                weeklyComparison = 0,
                isLoading = false,
                onViewAllSkillsClicked = {},
                onPromoClick = {},
                showSuggestions = true,
            )
        }

        // Check number text and color (White)
        composeTestRule.onNodeWithTag(TestTags.STATS_PROGRESS_NUMBER)
            .assertIsDisplayed()
            .assert(hasText("0%"))
            .assert(hasColor(Color.White))

        // Check icon absence
        composeTestRule.onNodeWithTag(TestTags.STATS_PROGRESS_ICON).assertDoesNotExist()
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
                flashcardsViewed = "1,234",
                currentStreak = "7",
                studyTime = "12h 30m",
                masteredDecks = "5",
                weeklyComparison = 12,
                isLoading = false,
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
                flashcardsViewed = "100",
                currentStreak = "5",
                studyTime = "2h",
                masteredDecks = "10",
                weeklyComparison = 0,
                isLoading = false,
                onViewAllSkillsClicked = {},
                onPromoClick = {},
                showSuggestions = true,
            )
        }

        composeTestRule.onNodeWithTag(TestTags.STATS_WEEKLY_ACTIVITY).assertIsDisplayed()
    }

    @Test
    fun testStatsScreen_emptyState_showsDefaultValues() {
        val weeklyActivityTitle = context.getString(R.string.stats_title_weekly_activity)
        val skillMasteryTitle = "Skill Mastery"
        val atAGlanceTitle = context.getString(R.string.stats_at_glance)

        composeTestRule.setContent {
            StatsContent(
                promoDeck = null,
                weeklyActivity = listOf(0, 0, 0, 0, 0, 0, 0),
                skillMastery = emptyList(),
                flashcardsViewed = "0",
                currentStreak = "0 Days",
                studyTime = "0m",
                masteredDecks = "0%",
                weeklyComparison = 0,
                isLoading = false,
                onViewAllSkillsClicked = {},
                onPromoClick = {},
                showSuggestions = true,
            )
        }

        // Verify section titles are displayed
        composeTestRule.onNodeWithText(weeklyActivityTitle).assertIsDisplayed()
        composeTestRule.onNodeWithText(skillMasteryTitle).assertIsDisplayed()
        composeTestRule.onNodeWithText(atAGlanceTitle).assertIsDisplayed()

        // Verify At-A-Glance default values
        // We use onAllNodesWithText and check existence because there are multiple "0" and "0%" nodes
        composeTestRule.onAllNodesWithText("0").fetchSemanticsNodes().isNotEmpty()
        composeTestRule.onNodeWithText("0 Days").assertIsDisplayed()
        composeTestRule.onNodeWithText("0m").assertIsDisplayed()
        composeTestRule.onAllNodesWithText("0%").fetchSemanticsNodes().isNotEmpty()

        // Verify Weekly Activity graph is still shown
        composeTestRule.onNodeWithTag(TestTags.STATS_WEEKLY_ACTIVITY).assertIsDisplayed()

        // Verify PromoWidget is not shown when promoDeck is null
        val startNowText = context.getString(R.string.promo_widget_confirm)
        composeTestRule.onNodeWithText(startNowText).assertDoesNotExist()
    }

    @Test
    fun testStatsScreen_clickWeeklyActivityInfo_showsDialog() {
        composeTestRule.setContent {
            StatsContent(
                promoDeck = null,
                weeklyActivity = listOf(10, 20, 30, 40, 50, 60, 70),
                skillMastery = emptyList(),
                flashcardsViewed = "100",
                currentStreak = "5",
                studyTime = "2h",
                masteredDecks = "10",
                weeklyComparison = 0,
                isLoading = false,
                onViewAllSkillsClicked = {},
                onPromoClick = {},
                showSuggestions = true,
            )
        }

        // Click the info icon
        composeTestRule.onNodeWithTag(TestTags.STATS_WEEKLY_ACTIVITY_DESCRIPTION).performClick()

        // Verify dialog title and description
        val expectedTitle = context.getString(R.string.stats_weekly_activity_info_title)
        val expectedDesc = context.getString(R.string.stats_weekly_activity_info_desc)
        composeTestRule.onNodeWithText(expectedTitle).assertIsDisplayed()
        composeTestRule.onNodeWithText(expectedDesc).assertIsDisplayed()

        // Close dialog
        composeTestRule.onNodeWithText(context.getString(R.string.stats_info_dialog_confirm)).performClick()
        composeTestRule.onNodeWithText(expectedTitle).assertDoesNotExist()
    }

    @Test
    fun testStatsScreen_clickSkillMasteryInfo_showsDialog() {
        composeTestRule.setContent {
            StatsContent(
                promoDeck = null,
                weeklyActivity = listOf(10, 20, 30, 40, 50, 60, 70),
                skillMastery = emptyList(),
                flashcardsViewed = "100",
                currentStreak = "5",
                studyTime = "2h",
                masteredDecks = "10",
                weeklyComparison = 0,
                isLoading = false,
                onViewAllSkillsClicked = {},
                onPromoClick = {},
                showSuggestions = true,
            )
        }

        // Click the info icon
        composeTestRule.onNodeWithTag(TestTags.STATS_SKILL_MASTERY_DESCRIPTION).performClick()

        // Verify dialog title and description
        val expectedTitle = context.getString(R.string.stats_skill_mastery_info_title)
        val expectedDesc = context.getString(R.string.stats_skill_mastery_info_desc)
        composeTestRule.onNodeWithText(expectedTitle).assertIsDisplayed()
        composeTestRule.onNodeWithText(expectedDesc).assertIsDisplayed()

        // Close dialog
        composeTestRule.onNodeWithText(context.getString(R.string.stats_info_dialog_confirm)).performClick()
        composeTestRule.onNodeWithText(expectedTitle).assertDoesNotExist()
    }
}
