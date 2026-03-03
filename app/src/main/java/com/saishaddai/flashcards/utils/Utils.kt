package com.saishaddai.flashcards.utils

import androidx.annotation.StringRes
import com.saishaddai.flashcards.R
import com.saishaddai.flashcards.model.Deck
import com.saishaddai.flashcards.model.Flashcard

fun List<Flashcard>.random(size: Int): List<Flashcard> = shuffled().take(size)

@StringRes
fun Deck.getMasteryLevel(): Int {
    return when (mastery) {
        in -1 .. 0 -> R.string.mastery_level_not_started
        in 1..25 -> R.string.mastery_level_just_started
        in 26..75 -> R.string.mastery_level_getting_there
        else -> R.string.mastery_level_mastered
    }
}
