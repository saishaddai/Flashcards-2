package com.saishaddai.flashcards.repository

import android.content.Context
import com.saishaddai.flashcards.model.DeckType.ANDROID_CORE
import com.saishaddai.flashcards.model.DeckType.ANDROID_OPS
import com.saishaddai.flashcards.model.DeckType.COMPOSE
import com.saishaddai.flashcards.model.DeckType.COROUTINES
import com.saishaddai.flashcards.model.DeckType.DAGGER_HILT
import com.saishaddai.flashcards.model.DeckType.DATABASES
import com.saishaddai.flashcards.model.DeckType.DESIGN_PATTERNS
import com.saishaddai.flashcards.model.DeckType.FIREBASE
import com.saishaddai.flashcards.model.DeckType.GRADLE
import com.saishaddai.flashcards.model.DeckType.GRAPHQL
import com.saishaddai.flashcards.model.DeckType.JETPACK
import com.saishaddai.flashcards.model.DeckType.KOTLIN
import com.saishaddai.flashcards.model.DeckType.KOTLIN_MP
import com.saishaddai.flashcards.model.DeckType.LIBRARIES
import com.saishaddai.flashcards.model.DeckType.MATERIAL_3
import com.saishaddai.flashcards.model.DeckType.NAVIGATION
import com.saishaddai.flashcards.model.DeckType.OOP
import com.saishaddai.flashcards.model.DeckType.TESTING
import com.saishaddai.flashcards.model.Flashcard
import com.saishaddai.flashcards.model.fcdata.androidCards
import com.saishaddai.flashcards.model.fcdata.composeCards
import com.saishaddai.flashcards.model.fcdata.coroutinesCards
import com.saishaddai.flashcards.model.fcdata.databaseCards
import com.saishaddai.flashcards.model.fcdata.diCards
import com.saishaddai.flashcards.model.fcdata.jetpackCards
import com.saishaddai.flashcards.model.fcdata.kotlinCards
import com.saishaddai.flashcards.model.fcdata.librariesCards
import com.saishaddai.flashcards.model.fcdata.navigationCards
import com.saishaddai.flashcards.model.fcdata.oopCards
import com.saishaddai.flashcards.model.fcdata.patternsCards
import com.saishaddai.flashcards.utils.random
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json

class HardcodedFlashcardRepository(private val context: Context? = null): FlashcardRepository<Flashcard>{

    override suspend fun getData(id: Int, size: Int): List<Flashcard> = withContext(Dispatchers.IO) {
        getFlashcardsForDeck(id).random(size)
    }

    override suspend fun getFlashcardsForDeck(deckId: Int): List<Flashcard> {
        return when (deckId) {
            OOP.id -> oopCards
            ANDROID_CORE.id -> androidCards
            KOTLIN.id -> kotlinCards
            KOTLIN_MP.id -> getListFromJson(context, KOTLIN_MP.jsonFile)
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
            GRAPHQL.id -> getListFromJson(context, GRAPHQL.jsonFile)
            else -> emptyList()
        }
    }

    private fun getListFromJson(context: Context?, fileName: String) : List<Flashcard> {
        return context?.let{
            loadFlashcardsFromJson(it, fileName)
        }?: emptyList()
    }
    private fun loadFlashcardsFromJson(context: Context, fileName: String): List<Flashcard> {
        return try {
            val jsonString = context.assets.open("decks/$fileName")
                .bufferedReader()
                .use { it.readText() }
            Json.decodeFromString<List<Flashcard>>(jsonString)
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }


}