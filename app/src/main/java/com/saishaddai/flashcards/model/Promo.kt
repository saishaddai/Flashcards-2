package com.saishaddai.flashcards.model

data class Promo (
    val title: String,
    var description: String = "",
    val deckId: Int)

val promos  = listOf(
    Promo("Ready for a challenge?", deckId = 1),
    Promo("Ready for a start?", deckId = 2),
    Promo("Ready for a challenge?", deckId = 3),
).map {
    it.apply {
        description = "Learn ${decks.find { deck -> deck.id == it.deckId }?.name ?: ""}"
    }
}