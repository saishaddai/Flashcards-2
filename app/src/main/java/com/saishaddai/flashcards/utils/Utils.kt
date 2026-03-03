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
        in 1..25 -> R.string.mastery_level_novice
        in 26..50 -> R.string.mastery_level_sophomore
        in 51..75 -> R.string.mastery_level_experienced
        in 76..99 -> R.string.mastery_level_veteran
        else -> R.string.mastery_level_mastered
    }
}
