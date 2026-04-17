package com.saishaddai.flashcards.model

import kotlinx.serialization.Serializable
import com.saishaddai.flashcards.model.DeckType.OOP
import com.saishaddai.flashcards.model.DeckType.ANDROID_CORE
import com.saishaddai.flashcards.model.DeckType.KOTLIN
import com.saishaddai.flashcards.model.DeckType.KOTLIN_MP
import com.saishaddai.flashcards.model.DeckType.SECURITY
import com.saishaddai.flashcards.model.DeckType.COMPOSE
import com.saishaddai.flashcards.model.DeckType.DATABASES
import com.saishaddai.flashcards.model.DeckType.DAGGER_HILT
import com.saishaddai.flashcards.model.DeckType.MATERIAL_3
import com.saishaddai.flashcards.model.DeckType.NAVIGATION
import com.saishaddai.flashcards.model.DeckType.JETPACK
import com.saishaddai.flashcards.model.DeckType.TESTING
import com.saishaddai.flashcards.model.DeckType.GRADLE
import com.saishaddai.flashcards.model.DeckType.ANDROID_OPS
import com.saishaddai.flashcards.model.DeckType.LIBRARIES
import com.saishaddai.flashcards.model.DeckType.DESIGN_PATTERNS
import com.saishaddai.flashcards.model.DeckType.COROUTINES
import com.saishaddai.flashcards.model.DeckType.FIREBASE
import com.saishaddai.flashcards.model.DeckType.GRAPHQL

enum class DeckType(val id: Int) {
    OOP(1),
    ANDROID_CORE(2),
    KOTLIN(3),
    KOTLIN_MP(4),
    SECURITY(5),
    COMPOSE(6),
    DATABASES(7),
    DAGGER_HILT(8),
    MATERIAL_3(9),
    NAVIGATION(10),
    JETPACK(11),
    TESTING(12),
    GRADLE(13),
    ANDROID_OPS(14),
    LIBRARIES(15),
    DESIGN_PATTERNS(16),
    COROUTINES(17),
    FIREBASE(18),
    GRAPHQL(19)
}

@Serializable
data class Deck(
    val id: Int,
    val name: String,
    val longName: String,
    var mastery: Int = 0,
    var cardCount: Int = 0,
    val isSelected: Boolean = false
)

val decks = listOf(
    Deck(OOP.id, "OOP", "Object-Oriented Programming", isSelected = true),
    Deck(ANDROID_CORE.id, "Android Core", "Android Core Technologies"),
    Deck(KOTLIN.id, "Kotlin", "Kotlin Programming Language"),
    Deck(KOTLIN_MP.id, "Kotlin MP", "Kotlin Multiplatform"),
    Deck(SECURITY.id, "Security", "Android Security"),
    Deck(COMPOSE.id, "Compose", "Jetpack Compose UI"),
    Deck(DATABASES.id, "Databases", "Android Databases"),
    Deck(DAGGER_HILT.id, "Dagger/Hilt", "Android Dependency Injection"),
    Deck(MATERIAL_3.id, "Material 3", "Android Material Design"),
    Deck(NAVIGATION.id, "Navigation", "Android Navigation Component"),
    Deck(JETPACK.id, "Jetpack", "Android Jetpack"),
    Deck(TESTING.id, "Testing", "Android Testing"),
    Deck(GRADLE.id, "Gradle", "Android Gradle"),
    Deck(ANDROID_OPS.id, "Android OPS", "Continuous Integration/Deployment"),
    Deck(LIBRARIES.id, "Libraries", "Android Libraries"),
    Deck(DESIGN_PATTERNS.id, "Design Patterns", "Android Design Patterns"),
    Deck(COROUTINES.id, "Coroutines", "Android Coroutines"),
    Deck(FIREBASE.id, "Firebase", "Firebase Integration"),
    Deck(GRAPHQL.id, "GraphQL", "GraphQL Integration")
).map {
    it.apply {
        cardCount = getFlashcardsForDeck(it.id).size
        mastery = sessions.find { session -> session.deckId == it.id }?.currentXP ?: 0
    }
}


