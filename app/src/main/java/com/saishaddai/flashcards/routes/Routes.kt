package com.saishaddai.flashcards.routes

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

object Routes {

    @Serializable
    data object DeckList: NavKey

    @Serializable
    data class FlashcardList(val deckId: Int): NavKey

    @Serializable
    data object Instructions: NavKey

    @Serializable
    data object Error: NavKey
}