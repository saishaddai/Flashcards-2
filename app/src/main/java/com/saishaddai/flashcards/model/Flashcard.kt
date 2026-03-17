package com.saishaddai.flashcards.model

import com.saishaddai.flashcards.model.fcdata.androidCards
import com.saishaddai.flashcards.model.fcdata.kmpCards
import com.saishaddai.flashcards.model.fcdata.kotlinCards
import com.saishaddai.flashcards.model.fcdata.oopCards

data class Flashcard(
    val deckId: Int,
    val id: Int,
    val question: String,
    val answer: String,
    val discovered: Boolean = false
)

val flashcards = oopCards + androidCards + kotlinCards + kmpCards
