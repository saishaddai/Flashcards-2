package com.saishaddai.flashcards.screens

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.assertIsOff
import androidx.compose.ui.test.assertIsOn
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import androidx.compose.ui.test.performTouchInput
import androidx.compose.ui.test.swipeRight
import androidx.test.platform.app.InstrumentationRegistry
import com.saishaddai.flashcards.R
import com.saishaddai.flashcards.repository.UserSettings
import com.saishaddai.flashcards.utils.TestTags
import org.junit.Rule
import org.junit.Test

class SettingsScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val context = InstrumentationRegistry.getInstrumentation().targetContext

    private val defaultSettings = UserSettings(
        flashcardsPerSession = 20,
        dailyStudyGoal = 50,
        isDarkMode = true,
        studyReminders = true,
        notificationSound = false,
        preferredStudyTime = "09:00 PM",
        quickStart = false,
        showAnswers = false,
        showSuggestions = true
    )

    @Test
    fun settingsScreen_isLoading_showsFullLoader() {
        composeTestRule.setContent {
            SettingsScreenContent(
                isLoading = true,
                userSettings = null,
                onRestartMasteryClicked = {},
                onPreferredStudyTimeChanged = { _, _ -> },
                onFlashcardsPerSessionChanged = {},
                onDailyStudyGoalChanged = {},
                onQuickStartChanged = {},
                onShowAnswersChanged = {},
                onShowSuggestionsChanged = {},
                onStudyRemindersChanged = {},
                onNotificationSoundChanged = {}
            )
        }

        composeTestRule.onNodeWithTag(TestTags.FULL_LOADER).assertIsDisplayed()
    }

    @Test
    fun settingsScreen_initialState_showsSettings() {
        composeTestRule.setContent {
            SettingsScreenContent(
                isLoading = false,
                userSettings = defaultSettings,
                onRestartMasteryClicked = {},
                onPreferredStudyTimeChanged = { _, _ -> },
                onFlashcardsPerSessionChanged = {},
                onDailyStudyGoalChanged = {},
                onQuickStartChanged = {},
                onShowAnswersChanged = {},
                onShowSuggestionsChanged = {},
                onStudyRemindersChanged = {},
                onNotificationSoundChanged = {}
            )
        }

        val title = context.getString(R.string.settings_title)
        composeTestRule.onNodeWithText(title).assertIsDisplayed()
    }

    @Test
    fun settingsScreen_changingSlider_enablesResetButton() {
        composeTestRule.setContent {
            SettingsScreenContent(
                isLoading = false,
                userSettings = defaultSettings.copy(flashcardsPerSession = 21), // Modified to enable reset
                onRestartMasteryClicked = {},
                onPreferredStudyTimeChanged = { _, _ -> },
                onFlashcardsPerSessionChanged = {},
                onDailyStudyGoalChanged = {},
                onQuickStartChanged = {},
                onShowAnswersChanged = {},
                onShowSuggestionsChanged = {},
                onStudyRemindersChanged = {},
                onNotificationSoundChanged = {}
            )
        }

        val resetText = context.getString(R.string.settings_flashcards_per_session_reset)

        // The reset button should be enabled because flashcardsPerSession is 21 (default is 20)
        composeTestRule.onNodeWithText(resetText).assertIsEnabled()
    }

    @Test
    fun settingsScreen_disablingReminders_disablesDependentSettings() {
        composeTestRule.setContent {
            SettingsScreenContent(
                isLoading = false,
                userSettings = defaultSettings.copy(studyReminders = false),
                onRestartMasteryClicked = {},
                onPreferredStudyTimeChanged = { _, _ -> },
                onFlashcardsPerSessionChanged = {},
                onDailyStudyGoalChanged = {},
                onQuickStartChanged = {},
                onShowAnswersChanged = {},
                onShowSuggestionsChanged = {},
                onStudyRemindersChanged = {},
                onNotificationSoundChanged = {}
            )
        }

        // Reminders are OFF
        composeTestRule.onNodeWithTag(TestTags.SETTINGS_DAILY_REMINDERS + "_switch").assertIsOff()

        // Verify dependents are disabled
        composeTestRule.onNodeWithTag(TestTags.SETTINGS_NOTIFICATION_SOUND + "_switch").assertIsNotEnabled()
        
        val actionLabel = defaultSettings.preferredStudyTime
        composeTestRule.onNodeWithText(actionLabel).assertIsNotEnabled()
    }

    @Test
    fun settingsScreen_canScrollToFooter() {
        composeTestRule.setContent {
            SettingsScreenContent(
                isLoading = false,
                userSettings = defaultSettings,
                onRestartMasteryClicked = {},
                onPreferredStudyTimeChanged = { _, _ -> },
                onFlashcardsPerSessionChanged = {},
                onDailyStudyGoalChanged = {},
                onQuickStartChanged = {},
                onShowAnswersChanged = {},
                onShowSuggestionsChanged = {},
                onStudyRemindersChanged = {},
                onNotificationSoundChanged = {}
            )
        }

        // Use the actual version string from strings.xml
        val appVersion = context.getString(R.string.app_version)
        composeTestRule.onNodeWithText(appVersion, substring = true)
            .performScrollTo()
            .assertIsDisplayed()
    }
}
