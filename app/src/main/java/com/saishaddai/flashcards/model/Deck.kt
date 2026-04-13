package com.saishaddai.flashcards.model

import kotlinx.serialization.Serializable

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
    Deck(1, "OOP", "Object-Oriented Programming", isSelected = true),
    Deck(2, "Android Core", "Android Core Technologies"),
    Deck(3, "Kotlin", "Kotlin Programming Language"),
    Deck(4, "Kotlin MP", "Kotlin Multiplatform"),
    Deck(5, "Security", "Android Security"),
    Deck(6, "Compose", "Jetpack Compose UI"),
    Deck(7, "Databases", "Android Databases"),
    Deck(8, "Dagger/Hilt", "Android Dependency Injection"),
    Deck(9, "Material 3", "Android Material Design"),
    Deck(10, "Navigation", "Android Navigation Component"),
    Deck(11, "Jetpack", "Android Jetpack"),
    Deck(12, "Testing", "Android Testing"),
    Deck(13, "Gradle", "Android Gradle"),
    Deck(14, "Android OPS", "Continuous Integration/Deployment"),
    Deck(15, "Libraries", "Android Libraries"),
    Deck(16, "Design Patterns", "Android Design Patterns"),
    Deck(17, "Coroutines", "Android Coroutines"),
    Deck(18, "Firebase", "Firebase Integration"),
    Deck(19, "GraphQL", "GraphQL Integration")
).map {
    it.apply {
        cardCount = flashcards.count { flashcard -> flashcard.deckId == it.id }
        mastery = sessions.find { session -> session.deckId == it.id }?.currentXP ?: 0
    }
}
