package com.saishaddai.flashcards.utils

import androidx.annotation.StringRes
import com.saishaddai.flashcards.model.Deck
import com.saishaddai.flashcards.model.Flashcard
import com.saishaddai.flashcards.model.MasteryLevel

fun List<Flashcard>.random(size: Int): List<Flashcard> = shuffled().take(size)

@StringRes
fun Deck.getMasteryLevel(): Int {
    return MasteryLevel.fromProgress(this.mastery).nameRes
}
