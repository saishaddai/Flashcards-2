package com.saishaddai.flashcards.model

data class Deck(
    val id : Int,
    val name: String,
    val cardCount: Int,
    val mastery: Int,
    val isSelected: Boolean = false
)

val decks = listOf(
    Deck(1, "Programming", 124, 80),
    Deck(2, "Android Core", 210, 45, true),
    Deck(3, "Kotlin MP", 88, 12),
    Deck(4, "Security", 56, 0),
    Deck(5, "Compose", 150, 62),
    Deck(6, "Databases", 94, 33),
    Deck(7, "Dagger/Hilt", 20, 0),
    Deck(8, "Material 3", 4, 0)
)
