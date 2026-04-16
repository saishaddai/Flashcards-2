package com.saishaddai.flashcards.utils

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.vector.ImageVector
import com.saishaddai.flashcards.R
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class DeckAssetsTest {

    @Test
    fun `getIconForDeck lower boundary validation`() {
        assertEquals(R.drawable.oop_icon, DeckAssets.getIconForDeck(1))
    }

    @Test
    fun `getIconForDeck upper boundary validation`() {
        assertEquals(R.drawable.graphql_icon, DeckAssets.getIconForDeck(19))
    }

    @Test
    fun `getIconForDeck image vector mapping check`() {
        assertEquals(Icons.Default.PhoneAndroid, DeckAssets.getIconForDeck(4))
    }

    @Test
    fun `getIconForDeck default case for zero`() {
        assertEquals(Icons.Default.Add, DeckAssets.getIconForDeck(0))
    }

    @Test
    fun `getIconForDeck default case for negative integers`() {
        assertEquals(Icons.Default.Add, DeckAssets.getIconForDeck(-1))
    }

    @Test
    fun `getIconForDeck overflow boundary check`() {
        assertEquals(Icons.Default.Add, DeckAssets.getIconForDeck(20))
    }

    @Test
    fun `getIconForDeck integer maximum value edge case`() {
        assertEquals(Icons.Default.Add, DeckAssets.getIconForDeck(Int.MAX_VALUE))
    }

    @Test
    fun `getIconForDeck integer minimum value edge case`() {
        assertEquals(Icons.Default.Add, DeckAssets.getIconForDeck(Int.MIN_VALUE))
    }

    @Test
    fun `getIconForDeck exhaustive drawable resource mapping`() {
        val expectedMappings = mapOf(
            1 to R.drawable.oop_icon,
            2 to R.drawable.android_icon,
            3 to R.drawable.kotlin_icon,
            10 to R.drawable.navigation_icon,
            11 to R.drawable.jetpack_icon,
            12 to R.drawable.test_icon,
            13 to R.drawable.gradle_icon,
            14 to R.drawable.ops_icon,
            15 to R.drawable.libraries_icon,
            16 to R.drawable.patterns_icon,
            18 to R.drawable.firebase_icon,
            19 to R.drawable.graphql_icon
        )

        expectedMappings.forEach { (id, expectedResource) ->
            assertEquals("ID $id should map to resource $expectedResource", expectedResource, DeckAssets.getIconForDeck(id))
        }
    }

    @Test
    fun `getIconForDeck exhaustive vector asset mapping`() {
        val expectedMappings = mapOf(
            4 to Icons.Default.PhoneAndroid,
            5 to Icons.Default.Security,
            6 to Icons.Default.BubbleChart,
            7 to Icons.Default.Storage,
            8 to Icons.Default.Link,
            9 to Icons.Default.Palette,
            17 to Icons.Default.ForkRight
        )

        expectedMappings.forEach { (id, expectedVector) ->
            assertEquals("ID $id should map to ImageVector $expectedVector", expectedVector, DeckAssets.getIconForDeck(id))
        }
    }

    @Test
    fun `getIconForDeck return type integrity check`() {
        val drawableIds = listOf(1, 2, 3, 10, 11, 12, 13, 14, 15, 16, 18, 19)
        val vectorIds = listOf(4, 5, 6, 7, 8, 9, 17, 0, 20) // Including defaults

        drawableIds.forEach { id ->
            val result = DeckAssets.getIconForDeck(id)
            assertTrue("ID $id should return an Int (Resource ID), but was ${result::class.java}", result is Int)
        }

        vectorIds.forEach { id ->
            val result = DeckAssets.getIconForDeck(id)
            assertTrue("ID $id should return an ImageVector, but was ${result::class.java}", result is ImageVector)
        }
    }
}