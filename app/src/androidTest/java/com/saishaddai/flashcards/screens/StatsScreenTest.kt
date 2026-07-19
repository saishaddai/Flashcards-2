package com.saishaddai.flashcards.screens

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.test.SemanticsMatcher
import androidx.compose.ui.test.assert
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import androidx.test.platform.app.InstrumentationRegistry
import com.saishaddai.flashcards.R
import com.saishaddai.flashcards.model.Deck
import com.saishaddai.flashcards.ui.theme.ErrorRed
import com.saishaddai.flashcards.ui.theme.SuccessGreen
import com.saishaddai.flashcards.utils.TestTags
import com.saishaddai.flashcards.utils.UiState
import com.saishaddai.flashcards.viewmodel.StatsUiState
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
            StatsScreen(
                uiState = UiState.Loading,
                promoDeck = null,
                showSuggestions = true,
                onViewAllSkillsClicked = {},
                onInfoClick = { _, _ -> },
                onDismissInfoDialog = {},
                onRetry = {},
                onPromoClick = { _ -> },
            )
        }

        composeTestRule.onNodeWithTag(TestTags.FULL_LOADER).assertIsDisplayed()
    }

    @Test
    fun testStatsScreen_loadedState_showsContent() {
        val statsTitle = context.getString(R.string.stats_title)
        
        composeTestRule.setContent {
            StatsScreen(
                uiState = UiState.Success(
                    StatsUiState(
                        weeklyActivity = listOf(10, 20, 30, 40, 50, 60, 70),
                        flashcardsViewed = "100",
                        currentStreak = "5",
                        studyTime = "2h",
                        masteredDecks = "10"
                    )
                ),
                promoDeck = null,
                showSuggestions = true,
                onViewAllSkillsClicked = {},
                onInfoClick = { _, _ -> },
                onDismissInfoDialog = {},
                onRetry = {},
                onPromoClick = { _ -> },
            )
        }

        composeTestRule.onNodeWithTag(TestTags.FULL_LOADER).assertDoesNotExist()
        composeTestRule.onNodeWithText(statsTitle).assertIsDisplayed()
    }

    @Test
    fun testStatsScreen_weeklyComparison_positiveGrowth() {
        composeTestRule.setContent {
            StatsScreen(
                uiState = UiState.Success(
                    StatsUiState(
                        weeklyActivity = listOf(10, 20, 30, 40, 50, 60, 70), // Ensure container renders
                        weeklyComparison = 15
                    )
                ),
                promoDeck = null,
                showSuggestions = true,
                onViewAllSkillsClicked = {},
                onInfoClick = { _, _ -> },
                onDismissInfoDialog = {},
                onRetry = {},
                onPromoClick = { _ -> },
            )
        }

        // Check number text and color (Green)
        composeTestRule.onNodeWithTag(TestTags.STATS_PROGRESS_NUMBER)
            .assertIsDisplayed()
            .assert(hasText("+15%"))
            .assert(hasColor(SuccessGreen))

        // Check icon presence and color
        composeTestRule.onNodeWithTag(TestTags.STATS_PROGRESS_ICON)
            .assertIsDisplayed()
            .assert(hasColor(SuccessGreen))
    }

    @Test
    fun testStatsScreen_weeklyComparison_negativeGrowth() {
        composeTestRule.setContent {
            StatsScreen(
                uiState = UiState.Success(
                    StatsUiState(
                        weeklyActivity = listOf(10, 20, 30, 40, 50, 60, 70), // Ensure container renders
                        weeklyComparison = -8
                    )
                ),
                promoDeck = null,
                showSuggestions = true,
                onViewAllSkillsClicked = {},
                onInfoClick = { _, _ -> },
                onDismissInfoDialog = {},
                onRetry = {},
                onPromoClick = { _ -> },
            )
        }

        // Check number text and color (Red)
        composeTestRule.onNodeWithTag(TestTags.STATS_PROGRESS_NUMBER)
            .assertIsDisplayed()
            .assert(hasText("-8%"))
            .assert(hasColor(ErrorRed))

        // Check icon presence and color
        composeTestRule.onNodeWithTag(TestTags.STATS_PROGRESS_ICON)
            .assertIsDisplayed()
            .assert(hasColor(ErrorRed))
    }

    @Test
    fun testStatsScreen_weeklyComparison_zeroGrowth() {
        composeTestRule.setContent {
            StatsScreen(
                uiState = UiState.Success(
                    StatsUiState(
                        weeklyActivity = listOf(10, 20, 30, 40, 50, 60, 70), // Ensure container renders
                        weeklyComparison = 0
                    )
                ),
                promoDeck = null,
                showSuggestions = true,
                onViewAllSkillsClicked = {},
                onInfoClick = { _, _ -> },
                onDismissInfoDialog = {},
                onRetry = {},
                onPromoClick = { _ -> },
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
            StatsScreen(
                uiState = UiState.Success(
                    StatsUiState(
                        weeklyActivity = listOf(10, 20, 30, 42, 50, 60, 70),
                        flashcardsViewed = "1,234",
                        currentStreak = "7",
                        studyTime = "12h 30m",
                        masteredDecks = "5",
                        weeklyComparison = 12
                    )
                ),
                promoDeck = mockDeck,
                showSuggestions = true,
                onViewAllSkillsClicked = {},
                onInfoClick = { _, _ -> },
                onDismissInfoDialog = {},
                onRetry = {},
                onPromoClick = { _ -> promoClicked = true },
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
            StatsScreen(
                uiState = UiState.Success(
                    StatsUiState(
                        weeklyActivity = listOf(10, 20, 30, 40, 50, 60, 70)
                    )
                ),
                promoDeck = null,
                showSuggestions = true,
                onViewAllSkillsClicked = {},
                onInfoClick = { _, _ -> },
                onDismissInfoDialog = {},
                onRetry = {},
                onPromoClick = { _ -> },
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
            StatsScreen(
                uiState = UiState.Success(
                    StatsUiState(
                        weeklyActivity = listOf(0, 0, 0, 0, 0, 0, 0)
                    )
                ),
                promoDeck = null,
                showSuggestions = true,
                onViewAllSkillsClicked = {},
                onInfoClick = { _, _ -> },
                onDismissInfoDialog = {},
                onRetry = {},
                onPromoClick = { _ -> },
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
        var uiState by mutableStateOf<UiState<StatsUiState>>(
            UiState.Success(
                StatsUiState(
                    weeklyActivity = listOf(10, 20, 30, 40, 50, 60, 70)
                )
            )
        )
        composeTestRule.setContent {
            val state = uiState
            StatsScreen(
                uiState = state,
                promoDeck = null,
                showSuggestions = true,
                onViewAllSkillsClicked = {},
                onInfoClick = { title, desc -> 
                    if (state is UiState.Success) {
                        uiState = UiState.Success(state.data.copy(infoDialogContent = title to desc))
                    }
                },
                onDismissInfoDialog = { 
                    if (state is UiState.Success) {
                        uiState = UiState.Success(state.data.copy(infoDialogContent = null))
                    }
                },
                onRetry = {},
                onPromoClick = { _ -> },
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
        var uiState by mutableStateOf<UiState<StatsUiState>>(
            UiState.Success(
                StatsUiState(
                    weeklyActivity = listOf(10, 20, 30, 40, 50, 60, 70)
                )
            )
        )
        composeTestRule.setContent {
            val state = uiState
            StatsScreen(
                uiState = state,
                promoDeck = null,
                showSuggestions = true,
                onViewAllSkillsClicked = {},
                onInfoClick = { title, desc -> 
                    if (state is UiState.Success) {
                        uiState = UiState.Success(state.data.copy(infoDialogContent = title to desc))
                    }
                },
                onDismissInfoDialog = { 
                    if (state is UiState.Success) {
                        uiState = UiState.Success(state.data.copy(infoDialogContent = null))
                    }
                },
                onRetry = {},
                onPromoClick = { _ -> },
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

    @Test
    fun testStatsScreen_isError_showsErrorViewAndRetries() {
        var retryCalled = false
        val errorMessage = "Test Error"
        
        composeTestRule.setContent {
            StatsScreen(
                uiState = UiState.Error(errorMessage),
                promoDeck = null,
                showSuggestions = true,
                onViewAllSkillsClicked = {},
                onInfoClick = { _, _ -> },
                onDismissInfoDialog = {},
                onRetry = { retryCalled = true },
                onPromoClick = { _ -> },
            )
        }

        composeTestRule.onNodeWithText("Oops!").assertIsDisplayed()
        composeTestRule.onNodeWithText(errorMessage).assertIsDisplayed()
        composeTestRule.onNodeWithText("Retry").performClick()

        assertTrue("Retry callback should have been triggered", retryCalled)
    }

    @Test
    fun testStatsScreen_skillMastery_viewAllAndShowLess() {
        var isSkillsExpanded by mutableStateOf(false)
        val masteryData = listOf(
            MasteryData("Skill 1", 80, R.string.mastery_level_experienced, Color.Green),
            MasteryData("Skill 2", 60, R.string.mastery_level_sophomore, Color.Yellow),
            MasteryData("Skill 3", 40, R.string.mastery_level_novice, Color.Gray)
        )

        composeTestRule.setContent {
            StatsScreen(
                uiState = UiState.Success(
                    StatsUiState(
                        skillMastery = masteryData,
                        isSkillsExpanded = isSkillsExpanded
                    )
                ),
                promoDeck = null,
                showSuggestions = true,
                onViewAllSkillsClicked = { isSkillsExpanded = !isSkillsExpanded },
                onInfoClick = { _, _ -> },
                onDismissInfoDialog = {},
                onRetry = {},
                onPromoClick = { _ -> },
            )
        }

        // Initially only 2 skills should be shown (take(2))
        composeTestRule.onNodeWithText("Skill 1").assertIsDisplayed()
        composeTestRule.onNodeWithText("Skill 2").assertIsDisplayed()
        composeTestRule.onNodeWithText("Skill 3").assertDoesNotExist()

        // Click View All
        composeTestRule.onNodeWithTag(TestTags.STATS_SKILL_MASTERY_VIEW_ALL).performClick()
        
        // All skills should be shown
        composeTestRule.onNodeWithText("Skill 1").assertIsDisplayed()
        composeTestRule.onNodeWithText("Skill 2").assertIsDisplayed()
        composeTestRule.onNodeWithText("Skill 3").assertIsDisplayed()

        // Button text should change to "Show Less"
        val showLessText = context.getString(R.string.stats_skill_mastery_show_less)
        composeTestRule.onNodeWithText(showLessText).assertIsDisplayed()

        // Click Show Less
        composeTestRule.onNodeWithTag(TestTags.STATS_SKILL_MASTERY_VIEW_ALL).performClick()
        
        // Only 2 skills again
        composeTestRule.onNodeWithText("Skill 3").assertDoesNotExist()
    }

    @Test
    fun testStatsScreen_atAGlance_displaysCorrectValues() {
        composeTestRule.setContent {
            StatsScreen(
                uiState = UiState.Success(
                    StatsUiState(
                        flashcardsViewed = "1,500",
                        currentStreak = "10 Days",
                        studyTime = "5h 20m",
                        masteredDecks = "75%"
                    )
                ),
                promoDeck = null,
                showSuggestions = true,
                onViewAllSkillsClicked = {},
                onInfoClick = { _, _ -> },
                onDismissInfoDialog = {},
                onRetry = {},
                onPromoClick = { _ -> },
            )
        }

        composeTestRule.onNodeWithText("1,500").assertIsDisplayed()
        composeTestRule.onNodeWithText("10 Days").assertIsDisplayed()
        composeTestRule.onNodeWithText("5h 20m").assertIsDisplayed()
        composeTestRule.onNodeWithText("75%").assertIsDisplayed()
    }

    @Test
    fun testStatsScreen_showSuggestions_controlsPromoVisibility() {
        val mockDeck = Deck(id = 1, name = "Promo", longName = "Promo Deck")
        var showSuggestions by mutableStateOf(true)

        composeTestRule.setContent {
            StatsScreen(
                uiState = UiState.Success(StatsUiState()),
                promoDeck = mockDeck,
                showSuggestions = showSuggestions,
                onViewAllSkillsClicked = {},
                onInfoClick = { _, _ -> },
                onDismissInfoDialog = {},
                onRetry = {},
                onPromoClick = { _ -> },
            )
        }

        val startNowText = context.getString(R.string.promo_widget_confirm)

        // Should be displayed when showSuggestions is true
        composeTestRule.onNodeWithText(startNowText).performScrollTo().assertIsDisplayed()

        // Disable suggestions
        showSuggestions = false
        composeTestRule.waitForIdle()

        // Should not be displayed
        composeTestRule.onNodeWithText(startNowText).assertDoesNotExist()
    }

    @Test
    fun testStatsScreen_emptySkillsList_doesNotCrash() {
        composeTestRule.setContent {
            StatsScreen(
                uiState = UiState.Success(
                    StatsUiState(
                        skillMastery = emptyList()
                    )
                ),
                promoDeck = null,
                showSuggestions = true,
                onViewAllSkillsClicked = {},
                onInfoClick = { _, _ -> },
                onDismissInfoDialog = {},
                onRetry = {},
                onPromoClick = { _ -> },
            )
        }

        // Verify section title is still displayed
        composeTestRule.onNodeWithText("Skill Mastery").assertIsDisplayed()
        
        // No skills should be visible
        val viewAllText = context.getString(R.string.stats_skill_mastery_view_all)
        composeTestRule.onNodeWithText(viewAllText).assertIsDisplayed()
    }
}

