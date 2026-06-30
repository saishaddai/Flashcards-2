package com.saishaddai.flashcards.screens

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import com.saishaddai.flashcards.ui.theme.Flashcards2Theme
import com.saishaddai.flashcards.utils.TestTags
import org.junit.Rule
import org.junit.Test

class StatsVisualTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun weeklyActivityCard_displaysCorrectTotalAndChart() {
        val activityData = listOf(10, 20, 15, 30, 25, 40, 35)
        val expectedTotal = activityData.sum().toString()

        composeTestRule.setContent {
            Flashcards2Theme {
                WeeklyActivityCard(
                    activityData = activityData,
                    weeklyComparison = 12,
                ) {}
            }
        }

        // Verify total sum text
        composeTestRule.onNodeWithTag(TestTags.STATS_WEEKLY_ACTIVITY_TOTAL)
            .assertIsDisplayed()
        
        composeTestRule.onNodeWithText(expectedTotal).assertIsDisplayed()

        // Verify Vico Chart presence
        composeTestRule.onNodeWithTag(TestTags.STATS_WEEKLY_ACTIVITY).assertIsDisplayed()
        
        // Verify comparison percentage
        composeTestRule.onNodeWithTag(TestTags.STATS_PROGRESS_NUMBER)
            .assertIsDisplayed()
        
        composeTestRule.onNodeWithText("+12%").assertIsDisplayed()
    }

    @Test
    fun skillCard_rendersCorrectPercentage() {
        val testPercentage = 75
        val testTitle = "Kotlin"
        val testLevelRes = com.saishaddai.flashcards.R.string.mastery_level_veteran

        composeTestRule.setContent {
            Flashcards2Theme {
                SkillCard(
                    data = MasteryData(
                        title = testTitle,
                        percentage = testPercentage,
                        levelRes = testLevelRes,
                        color = androidx.compose.ui.graphics.Color.Blue
                    )
                )
            }
        }

        // Verify title and level
        composeTestRule.onNodeWithText(testTitle).assertIsDisplayed()
        composeTestRule.onNodeWithText("Veteran").assertIsDisplayed()
        
        // Verify percentage text inside the progress indicator
        composeTestRule.onNodeWithText("$testPercentage%").assertIsDisplayed()
    }
}
