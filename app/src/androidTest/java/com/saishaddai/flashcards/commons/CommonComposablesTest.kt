package com.saishaddai.flashcards.commons

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.saishaddai.flashcards.screens.commons.BlueButton
import com.saishaddai.flashcards.screens.commons.ErrorView
import com.saishaddai.flashcards.screens.commons.TransparentButton
import com.saishaddai.flashcards.utils.TestTags
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class CommonComposablesTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun blueButton_displaysTextAndCallsOnClick() {
        var clicked = false
        val buttonText = "Click Me"
        
        composeTestRule.setContent {
            BlueButton(
                icon = Icons.Default.Add,
                text = buttonText,
                onClick = { clicked = true }
            )
        }

        composeTestRule.onNodeWithText(buttonText).assertIsDisplayed()
        composeTestRule.onNodeWithTag(TestTags.BLUE_BUTTON).performClick()
        assertTrue(clicked)
    }

    @Test
    fun blueButton_respectsEnabledState() {
        var clicked = false
        val buttonText = "Disabled"

        composeTestRule.setContent {
            BlueButton(
                icon = Icons.Default.Add,
                text = buttonText,
                enabled = false,
                onClick = { clicked = true }
            )
        }

        composeTestRule.onNodeWithTag(TestTags.BLUE_BUTTON).assertIsNotEnabled()
        composeTestRule.onNodeWithTag(TestTags.BLUE_BUTTON).performClick()
        assertTrue(!clicked)
    }

    @Test
    fun transparentButton_displaysTextAndCallsOnClick() {
        var clicked = false
        val buttonText = "Transparent"

        composeTestRule.setContent {
            TransparentButton(
                icon = Icons.Default.Add,
                text = buttonText,
                onClick = { clicked = true }
            )
        }

        composeTestRule.onNodeWithText(buttonText).assertIsDisplayed()
        composeTestRule.onNodeWithTag(TestTags.TRANSPARENT_BUTTON).performClick()
        assertTrue(clicked)
    }

    @Test
    fun errorView_displaysMessageAndCallsRetry() {
        var retryClicked = false
        val errorMessage = "Something went wrong"

        composeTestRule.setContent {
            ErrorView(
                message = errorMessage,
                onRetry = { retryClicked = true }
            )
        }

        composeTestRule.onNodeWithTag(TestTags.ERROR_VIEW).assertIsDisplayed()
        composeTestRule.onNodeWithTag(TestTags.ERROR_VIEW_TITLE).assertIsDisplayed()
        composeTestRule.onNodeWithText(errorMessage).assertIsDisplayed()
        
        composeTestRule.onNodeWithTag(TestTags.ERROR_VIEW_RETRY_BUTTON).performClick()
        assertTrue(retryClicked)
    }
}
