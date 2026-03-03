package com.saishaddai.flashcards.model

data class Promo (val title: String, val description: String, val deckId: Int)

val promos  = listOf(
    Promo("Ready for a challenge?", "Learn about OOP", 1),
    Promo("Ready for a start?", "Learn Android Core", 2),
    Promo("Ready for a challenge?", "Learn Kotlin", 3),
)