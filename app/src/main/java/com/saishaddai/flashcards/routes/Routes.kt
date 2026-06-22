package com.saishaddai.flashcards.routes

import androidx.navigation3.runtime.NavKey
import com.saishaddai.flashcards.model.Deck
import kotlinx.serialization.Serializable

sealed interface Route : NavKey {

    @Serializable
    data object DeckList : Route

    @Serializable
    data object Instructions : Route

    @Serializable
    data object Stats : Route

    @Serializable
    data object Settings : Route

    @Serializable
    data class FlashcardSession(val deck: Deck) : Route

    @Serializable
    data class FinishSession(
        val deck: Deck,
        val cardsReviewed: Int,
        val startTime: Long,
        val endTime: Long
    ) : Route
}
