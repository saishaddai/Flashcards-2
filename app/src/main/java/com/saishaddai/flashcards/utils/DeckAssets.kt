package com.saishaddai.flashcards.utils

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.BubbleChart
import androidx.compose.material.icons.filled.ForkRight
import androidx.compose.material.icons.filled.Link
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.PhoneAndroid
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Storage
import com.saishaddai.flashcards.R

object DeckAssets {

    fun getIconForDeck(deckId: Int): Any {
        return when (deckId) {
            1 -> R.drawable.oop_icon
            2 -> R.drawable.android_icon
            3 -> R.drawable.kotlin_icon
            4 -> Icons.Default.PhoneAndroid
            5 -> Icons.Default.Security
            6 -> Icons.Default.BubbleChart
            7 -> Icons.Default.Storage
            8 -> Icons.Default.Link
            9 -> Icons.Default.Palette
            10 -> R.drawable.navigation_icon
            11 -> R.drawable.jetpack_icon
            12 -> R.drawable.test_icon
            13 -> R.drawable.gradle_icon
            14 -> R.drawable.ops_icon
            15 -> R.drawable.libraries_icon
            16 -> R.drawable.patterns_icon
            17 -> Icons.Default.ForkRight
            18 -> R.drawable.firebase_icon
            19 -> R.drawable.graphql_icon
            else -> Icons.Default.Add
        }
    }
}