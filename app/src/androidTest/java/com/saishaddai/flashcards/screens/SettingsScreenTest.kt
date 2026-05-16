package com.saishaddai.flashcards.screens

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.assertIsOff
import androidx.compose.ui.test.assertIsOn
import androidx.compose.ui.test.hasAnyAncestor
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import androidx.compose.ui.test.performTouchInput
import androidx.compose.ui.test.swipeRight
import androidx.test.platform.app.InstrumentationRegistry
import com.saishaddai.flashcards.R
import com.saishaddai.flashcards.repository.SettingsRepository
import com.saishaddai.flashcards.repository.UserSettings
import com.saishaddai.flashcards.viewmodel.SettingsViewModel
import com.saishaddai.flashcards.utils.TestTags
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

class SettingsScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val context = InstrumentationRegistry.getInstrumentation().targetContext

    // Fake Repository to track calls
    private class FakeSettingsRepository : SettingsRepository {
        var restartCalled = false
        val settings = MutableStateFlow(
            UserSettings(
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
        )

        override suspend fun restartMasteryExperience() {
            restartCalled = true
        }
        override suspend fun saveFlashcardsPerSession(count: Int) {
            settings.value = settings.value.copy(flashcardsPerSession = count)
        }
        override suspend fun saveDailyStudyGoal(count: Int) {
            settings.value = settings.value.copy(dailyStudyGoal = count)
        }
        override suspend fun saveDarkMode(enabled: Boolean) {
            settings.value = settings.value.copy(isDarkMode = enabled)
        }
        override suspend fun saveStudyReminders(enabled: Boolean) {
            settings.value = settings.value.copy(studyReminders = enabled)
        }
        override suspend fun saveNotificationSound(enabled: Boolean) {
            settings.value = settings.value.copy(notificationSound = enabled)
        }
        override suspend fun savePreferredStudyTime(time: String) {
            settings.value = settings.value.copy(preferredStudyTime = time)
        }
        override suspend fun saveQuickStart(enabled: Boolean) {
            settings.value = settings.value.copy(quickStart = enabled)
        }
        override suspend fun saveShowAnswers(enabled: Boolean) {
            settings.value = settings.value.copy(showAnswers = enabled)
        }
        override suspend fun saveShowSuggestions(enabled: Boolean) {
            settings.value = settings.value.copy(showSuggestions = enabled)
        }
        override fun getSettings(): Flow<UserSettings> = settings
    }

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
        val fakeRepository = FakeSettingsRepository()
        val viewModel = SettingsViewModel(fakeRepository)
        
        composeTestRule.setContent {
            SettingsScreen(viewModel = viewModel)
        }

        val title = context.getString(R.string.settings_title)
        composeTestRule.onNodeWithText(title).assertIsDisplayed()
    }

    @Test
    fun settingsScreen_changingSlider_enablesResetButton() {
        val fakeRepository = FakeSettingsRepository()
        val viewModel = SettingsViewModel(fakeRepository)

        composeTestRule.setContent { SettingsScreen(viewModel = viewModel) }

        val resetText = context.getString(R.string.settings_flashcards_per_session_reset)

        // 1. Initially, the button should be disabled (assuming defaults are 20 and 50)
        composeTestRule.onNodeWithText(resetText).assertIsNotEnabled()

        // 2. Find the slider for "Flashcards per session" and perform a drag/swipe
        composeTestRule.onNodeWithTag("slider_session").performTouchInput {
            swipeRight()
        }

        // 3. The button should now be enabled
        composeTestRule.onNodeWithText(resetText).assertIsEnabled()

        // 4. Clicking reset should disable it again
        composeTestRule.onNodeWithText(resetText).performClick()
        composeTestRule.onNodeWithText(resetText).assertIsNotEnabled()
    }

    @Test
    fun settingsScreen_restartMastery_showsDialogAndCallsViewModel() {
        val fakeRepository = FakeSettingsRepository()
        val viewModel = SettingsViewModel(fakeRepository)
        
        composeTestRule.setContent { SettingsScreen(viewModel = viewModel) }

        // 1. Scroll to the bottom to find the restart button
        composeTestRule.onNodeWithText(context.getString(R.string.settings_system_restart))
            .performScrollTo()
            .performClick()

        // 2. Verify Dialog Title is displayed
        composeTestRule.onNode(hasText(context.getString(R.string.settings_system_restart)) and hasAnyAncestor(hasTestTag("restart_dialog")))
            .assertIsDisplayed()

        // 3. Click Confirm in Dialog
        composeTestRule.onNodeWithText(context.getString(R.string.settings_restart_dialog_confirm))
            .performClick()

        // 4. Verify Dialog is dismissed
        composeTestRule.onNodeWithText(context.getString(R.string.settings_restart_dialog_confirm))
            .assertDoesNotExist()
            
        // 5. Verify repository was called
        assertTrue(fakeRepository.restartCalled)
    }

    @Test
    fun settingsScreen_toggleSwitches_updatesUI() {
        val fakeRepository = FakeSettingsRepository()
        val viewModel = SettingsViewModel(fakeRepository)
        
        composeTestRule.setContent { SettingsScreen(viewModel = viewModel) }

        // Find the switch by tag
        composeTestRule.onNodeWithTag(TestTags.SETTINGS_STUDY_REMINDERS + "_switch")
            .assertIsOn()
            .performClick()
            .assertIsOff()
    }

    @Test
    fun settingsScreen_toggleShowAnswers_updatesUI() {
        val fakeRepository = FakeSettingsRepository()
        val viewModel = SettingsViewModel(fakeRepository)

        composeTestRule.setContent { SettingsScreen(viewModel = viewModel) }

        // Initially it should be off (false by default)
        composeTestRule.onNodeWithTag(TestTags.SETTINGS_SHOW_ANSWERS + "_switch")
            .assertIsOff()
            .performClick()
            .assertIsOn()
    }

    @Test
    fun settingsScreen_canScrollToFooter() {
        val fakeRepository = FakeSettingsRepository()
        val viewModel = SettingsViewModel(fakeRepository)
        
        composeTestRule.setContent { SettingsScreen(viewModel = viewModel) }

        // Use the actual version string from strings.xml
        val appVersion = context.getString(R.string.app_version)
        composeTestRule.onNodeWithText(appVersion, substring = true)
            .performScrollTo()
            .assertIsDisplayed()
    }

    @Test
    fun settingsScreen_clickStudyTime_triggersAction() {
        val fakeRepository = FakeSettingsRepository()
        val viewModel = SettingsViewModel(fakeRepository)
        
        composeTestRule.setContent { SettingsScreen(viewModel = viewModel) }

        composeTestRule.onNodeWithText(context.getString(R.string.settings_preferred_study_time))
            .performClick()
    }
}
