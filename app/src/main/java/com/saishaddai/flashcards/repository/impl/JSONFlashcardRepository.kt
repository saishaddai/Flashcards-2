package com.saishaddai.flashcards.repository.impl

import android.content.Context
import com.saishaddai.flashcards.model.DeckType
import com.saishaddai.flashcards.model.DeckType.ANDROID_CORE
import com.saishaddai.flashcards.model.DeckType.COROUTINES
import com.saishaddai.flashcards.model.DeckType.DESIGN_PATTERNS
import com.saishaddai.flashcards.model.DeckType.JETPACK
import com.saishaddai.flashcards.model.DeckType.GRADLE
import com.saishaddai.flashcards.model.DeckType.OOP
import com.saishaddai.flashcards.model.DeckType.KOTLIN
import com.saishaddai.flashcards.model.DeckType.KOTLIN_MP
import com.saishaddai.flashcards.model.DeckType.FIREBASE
import com.saishaddai.flashcards.model.DeckType.MATERIAL_3
import com.saishaddai.flashcards.model.DeckType.ANDROID_OPS
import com.saishaddai.flashcards.model.DeckType.LIBRARIES
import com.saishaddai.flashcards.model.DeckType.NAVIGATION
import com.saishaddai.flashcards.model.DeckType.SECURITY
import com.saishaddai.flashcards.model.DeckType.TESTING
import com.saishaddai.flashcards.model.Flashcard
import com.saishaddai.flashcards.model.fcdata.composeCards
import com.saishaddai.flashcards.model.fcdata.databaseCards
import com.saishaddai.flashcards.model.fcdata.diCards
import com.saishaddai.flashcards.repository.FlashcardRepository
import com.saishaddai.flashcards.utils.random
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json

class JSONFlashcardRepository(private val context: Context? = null) :
    FlashcardRepository<Flashcard> {

    val countMap: MutableMap<Int, Int> = mutableMapOf()

    override suspend fun getData(id: Int, size: Int): List<Flashcard> =
        withContext(Dispatchers.IO) {
            getFlashcardsForDeck(id).random(size)
        }

    override suspend fun getDataCount(id: Int): Int {
        return countMap[id] ?: getFlashcardsForDeck(id).size.also {
            countMap[id] = it
        }
    }

    fun getFlashcardsForDeck(deckId: Int): List<Flashcard> {
        return when (deckId) {
            OOP.id -> getListFromJson(context, OOP)
            ANDROID_CORE.id -> getListFromJson(context, ANDROID_CORE)
            KOTLIN.id -> getListFromJson(context, KOTLIN)
            KOTLIN_MP.id -> getListFromJson(context, KOTLIN_MP)
            SECURITY.id -> getListFromJson(context, SECURITY)
            DeckType.COMPOSE.id -> composeCards.storeCount(DeckType.COMPOSE.id)
            DeckType.DATABASES.id -> databaseCards.storeCount(DeckType.DATABASES.id)
            DeckType.DAGGER_HILT.id -> diCards.storeCount(DeckType.DAGGER_HILT.id)
            MATERIAL_3.id -> getListFromJson(context, MATERIAL_3)
            NAVIGATION.id -> getListFromJson(context, NAVIGATION)
            JETPACK.id -> getListFromJson(context, JETPACK)
            TESTING.id -> getListFromJson(context, TESTING)
            GRADLE.id -> getListFromJson(context, GRADLE)
            ANDROID_OPS.id -> getListFromJson(context, ANDROID_OPS)
            LIBRARIES.id -> getListFromJson(context, LIBRARIES)
            DESIGN_PATTERNS.id -> getListFromJson(context, DESIGN_PATTERNS)
            COROUTINES.id -> getListFromJson(context, COROUTINES)
            FIREBASE.id -> getListFromJson(context, FIREBASE)
            DeckType.GRAPHQL.id -> getListFromJson(context, DeckType.GRAPHQL)
            else -> emptyList()
        }
    }


    private fun getListFromJson(context: Context?, deckType : DeckType) : List<Flashcard> {
        return context?.let {
            loadFlashcardsFromJson(it, deckType.jsonFile)
                .storeCount(deckType.id)
        } ?: emptyList()
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

    private fun List<Flashcard>.storeCount(deckId: Int): List<Flashcard> {
        countMap[deckId] = this.size
        return this
    }


}