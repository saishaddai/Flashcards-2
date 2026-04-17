package com.saishaddai.flashcards.model

import com.saishaddai.flashcards.model.fcdata.androidCards
import com.saishaddai.flashcards.model.fcdata.composeCards
import com.saishaddai.flashcards.model.fcdata.coroutinesCards
import com.saishaddai.flashcards.model.fcdata.databaseCards
import com.saishaddai.flashcards.model.fcdata.diCards
import com.saishaddai.flashcards.model.fcdata.jetpackCards
import com.saishaddai.flashcards.model.fcdata.kmpCards
import com.saishaddai.flashcards.model.fcdata.kotlinCards
import com.saishaddai.flashcards.model.fcdata.librariesCards
import com.saishaddai.flashcards.model.fcdata.navigationCards
import com.saishaddai.flashcards.model.fcdata.oopCards
import com.saishaddai.flashcards.model.fcdata.patternsCards

data class Flashcard(
    val deckId: Int,
    val id: Int,
    val question: String,
    val answer: String
)

val flashcards = oopCards +
        androidCards +
        kotlinCards +
        kmpCards +
        diCards +
        composeCards +
        patternsCards +
        databaseCards +
        navigationCards +
        coroutinesCards +
        jetpackCards +
        librariesCards
