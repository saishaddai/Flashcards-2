package com.saishaddai.flashcards.utils

import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

// 1. Create a dummy NavKey for testing
@Serializable
data object TestRoute : NavKey

@Serializable
data object AnotherRoute : NavKey

class NavigationUtilsTest {

    @Test
    fun `navigateTo adds route to backstack`() {
        val backStack = NavBackStack<NavKey>(TestRoute)
        backStack.navigateTo(AnotherRoute)

        assertEquals(2, backStack.size)
        assertEquals(AnotherRoute, backStack.lastOrNull())
    }

    @Test
    fun `navigateBack removes the last route`() {
        val backStack = NavBackStack<NavKey>(TestRoute)
        backStack.navigateTo(AnotherRoute)
        backStack.navigateBack()

        assertEquals(1, backStack.size)
        assertEquals(TestRoute, backStack.lastOrNull())
    }

    @Test
    fun `navigateBack on empty stack returns null`() {
        // NavBackStack initialized with TestRoute has 1 element.
        val backStack = NavBackStack<NavKey>(TestRoute)
        backStack.navigateBack() // Removes TestRoute, size is 0
        val result = backStack.navigateBack() // Stack is empty

        assertNull(result)
    }
}
