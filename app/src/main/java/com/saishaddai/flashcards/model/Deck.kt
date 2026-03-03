package com.saishaddai.flashcards.model

data class Deck(
    val id : Int,
    val name: String,
    val mastery: Int,
    var cardCount: Int = 0,
    val isSelected: Boolean = false
)

val decks = listOf(
    Deck(1, "OOP", 80),
    Deck(2, "Android Core", 45, isSelected = true),
    Deck(3, "Kotlin", 45),
    Deck(4, "Kotlin MP", 12),
    Deck(5, "Security", 0),
    Deck(6, "Compose", 62),
    Deck(7, "Databases", 33),
    Deck(8, "Dagger/Hilt", 0),
    Deck(9, "Material 3", 0),
    Deck(10, "Navigation", 0),
    Deck(11, "Jetpack", 0),
    Deck(12, "Unit Test", 0),
    Deck(13, "Gradle", 0),
    Deck(14, "Android OPS", 0),
    Deck(15, "Libraries", 0),
    Deck(16, "Design Patterns", 0),
    Deck(17, "Coroutines", 0)
).map {
    it.apply {
        cardCount = flashcards.count { flashcard -> flashcard.deckId == it.id }
    }
}
