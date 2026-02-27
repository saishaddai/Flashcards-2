package com.saishaddai.flashcards.utils

import androidx.annotation.StringRes
import androidx.annotation.VisibleForTesting
import com.saishaddai.flashcards.R
import com.saishaddai.flashcards.model.Deck
import com.saishaddai.flashcards.model.Flashcard

fun List<Flashcard>.random(size: Int): List<Flashcard> = shuffled().take(size)

@StringRes
fun Deck.getMasteryLevel(): Int {
    return when (mastery) {
        in 0..25 -> R.string.mastery_level_just_started
        in 26..75 -> R.string.mastery_level_getting_there
        else -> R.string.mastery_level_mastered
    }
}
