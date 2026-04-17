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
import com.saishaddai.flashcards.model.DeckType.*

data class Flashcard(
    val deckId: Int,
    val id: Int,
    val question: String,
    val answer: String
)

fun getFlashcardsForDeck(deckId: Int): List<Flashcard> {
    return when (deckId) {
        OOP.id -> oopCards
        ANDROID_CORE.id -> androidCards
        KOTLIN.id -> kotlinCards
        KOTLIN_MP.id -> kmpCards
        COMPOSE.id -> composeCards
        DATABASES.id -> databaseCards
        DAGGER_HILT.id -> diCards
        MATERIAL_3.id -> emptyList()
        NAVIGATION.id -> navigationCards
        JETPACK.id -> jetpackCards
        TESTING.id -> emptyList()
        GRADLE.id -> emptyList()
        ANDROID_OPS.id -> emptyList()
        LIBRARIES.id -> librariesCards
        DESIGN_PATTERNS.id -> patternsCards
        COROUTINES.id -> coroutinesCards
        FIREBASE.id -> emptyList()
        GRAPHQL.id -> emptyList()
        else -> emptyList()
    }
}

