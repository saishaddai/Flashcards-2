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



