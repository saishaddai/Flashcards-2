package com.saishaddai.flashcards.utils

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.BubbleChart
import androidx.compose.material.icons.filled.ForkRight
import androidx.compose.material.icons.filled.Cloud
import androidx.compose.material.icons.filled.Hub
import androidx.compose.material.icons.filled.Link
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.PhoneAndroid
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Sensors
import androidx.compose.material.icons.filled.Storage
import com.saishaddai.flashcards.R
import com.saishaddai.flashcards.model.DeckType.*

object DeckAssets {

    fun getIconForDeck(deckId: Int): Any {
        return when (deckId) {
            OOP.id -> R.drawable.oop_icon
            ANDROID_CORE.id -> R.drawable.android_icon
            KOTLIN.id -> R.drawable.kotlin_icon
            KOTLIN_MP.id -> Icons.Default.PhoneAndroid
            SECURITY.id -> Icons.Default.Security
            COMPOSE.id -> Icons.Default.BubbleChart
            DATABASES.id -> Icons.Default.Storage
            DI.id -> Icons.Default.Link
            MATERIAL_3.id -> Icons.Default.Palette
            NAVIGATION.id -> R.drawable.navigation_icon
            JETPACK.id -> R.drawable.jetpack_icon
            TESTING.id -> R.drawable.test_icon
            GRADLE.id -> R.drawable.gradle_icon
            ANDROID_OPS.id -> R.drawable.ops_icon
            LIBRARIES.id -> R.drawable.libraries_icon
            DESIGN_PATTERNS.id -> R.drawable.patterns_icon
            COROUTINES.id -> Icons.Default.ForkRight
            FIREBASE.id -> Icons.Default.Cloud
            GRAPHQL.id -> Icons.Default.Hub
            SENSORS.id -> Icons.Default.Sensors
            else -> Icons.Default.Add
        }
    }

    fun getDescriptionForDeck(deckId: Int): Int {
        return when (deckId) {
            OOP.id -> R.string.deck_description_oop
            ANDROID_CORE.id-> R.string.deck_description_kotlin
            KOTLIN.id -> R.string.deck_description_kotlin
            KOTLIN_MP.id -> R.string.deck_description_kmp
            SECURITY.id -> R.string.deck_description_security
            COMPOSE.id -> R.string.deck_description_compose
            DATABASES.id -> R.string.deck_description_databases
            DI.id -> R.string.deck_description_di
            MATERIAL_3.id -> R.string.deck_description_material3
            NAVIGATION.id -> R.string.deck_description_navigation
            JETPACK.id -> R.string.deck_description_jetpack
            TESTING.id -> R.string.deck_description_testing
            GRADLE.id -> R.string.deck_description_gradle
            ANDROID_OPS.id -> R.string.deck_description_ops
            LIBRARIES.id -> R.string.deck_description_libraries
            DESIGN_PATTERNS.id -> R.string.deck_description_design_patterns
            COROUTINES.id -> R.string.deck_description_coroutines
            FIREBASE.id -> R.string.deck_description_firebase
            GRAPHQL.id -> R.string.deck_description_graphql
            SENSORS.id -> R.string.deck_description_sensors
            else -> R.string.deck_description_generic
        }
    }

}