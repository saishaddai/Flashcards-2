package com.saishaddai.flashcards.navigation

import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import androidx.test.platform.app.InstrumentationRegistry
import com.saishaddai.flashcards.R
import com.saishaddai.flashcards.routes.Route
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

class NavigationTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val context = InstrumentationRegistry.getInstrumentation().targetContext

    @Test
    fun testInitialRoute_isDeckList() {
        val backStack = NavBackStack<NavKey>(Route.DeckList)
        
        composeTestRule.setContent {
            NavigationWrapper(backStack = backStack)
        }

        assertEquals(Route.DeckList, backStack.lastOrNull())
    }

    @Test
    fun testNavigation_bottomNavItems_updateBackStack() {
        val backStack = NavBackStack<NavKey>(Route.DeckList)

        composeTestRule.setContent {
            NavigationWrapper(backStack = backStack)
        }

        // Navigate to Instructions
        composeTestRule.onNodeWithText(context.getString(R.string.decks_bottom_nav_instructions)).performClick()
        assertTrue(backStack.lastOrNull() is Route.Instructions)

        // Navigate to Stats
        composeTestRule.onNodeWithText(context.getString(R.string.decks_bottom_nav_stats)).performClick()
        assertTrue(backStack.lastOrNull() is Route.Stats)

        // Navigate to Settings
        composeTestRule.onNodeWithText(context.getString(R.string.decks_bottom_nav_settings)).performClick()
        assertTrue(backStack.lastOrNull() is Route.Settings)

        // Navigate back to Learn (DeckList)
        composeTestRule.onNodeWithText(context.getString(R.string.decks_bottom_nav_learn)).performClick()
        assertTrue(backStack.lastOrNull() is Route.DeckList)
    }
}
