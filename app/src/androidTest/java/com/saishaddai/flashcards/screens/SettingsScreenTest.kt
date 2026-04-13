package com.saishaddai.flashcards.screens

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.assertIsOff
import androidx.compose.ui.test.assertIsOn
import androidx.compose.ui.test.hasParent
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.isOn
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
import com.saishaddai.flashcards.viewmodel.SettingsViewModel
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
        override suspend fun restartMasteryExperience() {
            restartCalled = true
        }
        override suspend fun saveFlashcardsPerSession(count: Int) {}
        override suspend fun saveDailyStudyGoal(count: Int) {}
        override suspend fun saveDarkMode(enabled: Boolean) {}
        override suspend fun saveStudyReminders(enabled: Boolean) {}
        override suspend fun saveNotificationSound(enabled: Boolean) {}
    }

    @Test
    fun settingsScreen_initialState_showsSettings() {
        composeTestRule.setContent {
            SettingsScreen()
        }

        val title = context.getString(R.string.settings_title)
        composeTestRule.onNodeWithText(title).assertIsDisplayed()
    }

    @Test
    fun settingsScreen_changingSlider_enablesResetButton() {
        composeTestRule.setContent { SettingsScreen() }

        val resetText = context.getString(R.string.settings_flashcards_per_session_reset)

        // 1. Initially, the button should be disabled (assuming defaults are 20 and 50)
        composeTestRule.onNodeWithText(resetText).assertIsNotEnabled()

        // 2. Find the slider for "Flashcards per session" and perform a drag/swipe
        // Note: You might need to add a modifier.testTag("slider_session") to the Slider in the source code
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

        // 1. Scroll to the bottom to find the restart button (it's in a Column with verticalScroll)
        composeTestRule.onNodeWithText(context.getString(R.string.settings_system_restart))
            .performScrollTo()
            .performClick()

        // 2. Verify Dialog Title is displayed
        composeTestRule.onNodeWithText(context.getString(R.string.settings_system_restart))
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
        composeTestRule.setContent { SettingsScreen() }

        val remindersText = context.getString(R.string.settings_daily_reminders)

        // Find the switch specifically (use hasAnyAncestor to distinguish from the text)
        composeTestRule.onNode(isOn() and hasParent(hasText(remindersText)))
            .assertIsOn()
            .performClick()
            .assertIsOff()
    }

    @Test
    fun settingsScreen_canScrollToFooter() {
        composeTestRule.setContent { SettingsScreen() }

        // Use the actual version string from strings.xml
        val appVersion = context.getString(R.string.app_version)
        composeTestRule.onNodeWithText(appVersion, substring = true)
            .performScrollTo()
            .assertIsDisplayed()
    }

    @Test
    fun settingsScreen_clickStudyTime_triggersAction() {
        // Since we don't have a specific way to track this in the basic FakeSettingsRepository yet,
        // we just ensure it doesn't crash and the element is clickable.
        composeTestRule.setContent { SettingsScreen() }

        composeTestRule.onNodeWithText(context.getString(R.string.settings_preferred_study_time))
            .performClick()
    }
}
