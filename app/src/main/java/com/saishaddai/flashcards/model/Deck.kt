package com.saishaddai.flashcards.model

data class Deck(
    val id: Int,
    val name: String,
    var mastery: Int = 0,
    var cardCount: Int = 0,
    val isSelected: Boolean = false
)

val decks = listOf(
    Deck(1, "OOP", isSelected = true),
    Deck(2, "Android Core"),
    Deck(3, "Kotlin"),
    Deck(4, "Kotlin MP"),
    Deck(5, "Security"),
    Deck(6, "Compose"),
    Deck(7, "Databases"),
    Deck(8, "Dagger/Hilt"),
    Deck(9, "Material 3"),
    Deck(10, "Navigation"),
    Deck(11, "Jetpack"),
    Deck(12, "Unit Test"),
    Deck(13, "Gradle"),
    Deck(14, "Android OPS"),
    Deck(15, "Libraries"),
    Deck(16, "Design Patterns"),
    Deck(17, "Coroutines")
).map {
    it.apply {
        cardCount = flashcards.count { flashcard -> flashcard.deckId == it.id }
        mastery = sessions.find { session -> session.deckId == it.id }?.currentXP ?: 0
    }
}
