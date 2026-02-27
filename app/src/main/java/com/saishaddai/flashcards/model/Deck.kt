package com.saishaddai.flashcards.model

data class Deck(
    val id : Int,
    val name: String,
    val cardCount: Int,
    val mastery: Int,
    val isSelected: Boolean = false
)

val decks = listOf(
    Deck(1, "OOP", 124, 80),
    Deck(2, "Android Core", 210, 45, true),
    Deck(3, "Kotlin", 90, 45),
    Deck(4, "Kotlin MP", 88, 12),
    Deck(5, "Security", 56, 0),
    Deck(6, "Compose", 150, 62),
    Deck(7, "Databases", 94, 33),
    Deck(8, "Dagger/Hilt", 20, 0),
    Deck(9, "Material 3", 4, 0),
    Deck(10, "Navigation", 4, 0),
    Deck(11, "Jetpack", 4, 0),
    Deck(12, "Unit Test", 4, 0),
    Deck(13, "Gradle", 4, 0),
    Deck(14, "Android OPS", 4, 0),
    Deck(15, "Libraries", 4, 0),
    Deck(16, "Design Patterns", 4, 0),
    Deck(17, "Coroutines", 4, 0)
)
