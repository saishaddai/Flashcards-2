package com.saishaddai.flashcards.data

data class Deck(
    val name: String,
    val cardCount: Int,
    val mastery: Int,
    val isSelected: Boolean = false
)

val decks = listOf(
    Deck("Programming", 124, 80),
    Deck("Android Core", 210, 45, true),
    Deck("Kotlin MP", 88, 12),
    Deck("Security", 56, 0),
    Deck("Compose", 150, 62),
    Deck("Databases", 94, 33),
    Deck("Dagger/Hilt", 0, 0),
    Deck("Material 3", 0, 0)
)
