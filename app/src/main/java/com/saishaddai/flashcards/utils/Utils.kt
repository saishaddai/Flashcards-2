package com.saishaddai.flashcards.utils

import androidx.annotation.VisibleForTesting
import com.saishaddai.flashcards.data.Flashcard

@VisibleForTesting
fun getRandomList(size: Int = list.size, list: List<Flashcard>): List<Flashcard> =
    list.shuffled().take(size)
