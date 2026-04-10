package com.saishaddai.flashcards.screens

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.test.platform.app.InstrumentationRegistry
import com.saishaddai.flashcards.R
import org.junit.Rule
import org.junit.Test

class SettingsScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val context = InstrumentationRegistry.getInstrumentation().targetContext

    @Test
    fun settingsScreen_initialState_showsSettings() {
        composeTestRule.setContent {
            SettingsScreen()
        }

        val title = context.getString(R.string.settings_title)
        composeTestRule.onNodeWithText(title).assertIsDisplayed()
    }
}
