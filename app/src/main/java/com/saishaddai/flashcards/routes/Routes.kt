package com.saishaddai.flashcards.routes

import androidx.navigation3.runtime.NavKey
import com.saishaddai.flashcards.model.Deck
import kotlinx.serialization.Serializable

object Routes {

    @Serializable
    data object DeckList : NavKey

    @Serializable
    data object Instructions : NavKey

    @Serializable
    data object Stats : NavKey

    @Serializable
    data object Settings : NavKey

    @Serializable
    data class FlashcardSession(val deck: Deck) : NavKey

    @Serializable
    data object FinishSession : NavKey

}