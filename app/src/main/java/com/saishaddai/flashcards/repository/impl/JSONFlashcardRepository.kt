package com.saishaddai.flashcards.repository.impl

import android.content.Context
import com.saishaddai.flashcards.model.DeckType
import com.saishaddai.flashcards.model.DeckType.ANDROID_CORE
import com.saishaddai.flashcards.model.DeckType.COROUTINES
import com.saishaddai.flashcards.model.DeckType.DESIGN_PATTERNS
import com.saishaddai.flashcards.model.DeckType.JETPACK
import com.saishaddai.flashcards.model.DeckType.GRADLE
import com.saishaddai.flashcards.model.DeckType.DI
import com.saishaddai.flashcards.model.DeckType.OOP
import com.saishaddai.flashcards.model.DeckType.KOTLIN
import com.saishaddai.flashcards.model.DeckType.KOTLIN_MP
import com.saishaddai.flashcards.model.DeckType.GRAPHQL
import com.saishaddai.flashcards.model.DeckType.COMPOSE
import com.saishaddai.flashcards.model.DeckType.FIREBASE
import com.saishaddai.flashcards.model.DeckType.MATERIAL_3
import com.saishaddai.flashcards.model.DeckType.ANDROID_OPS
import com.saishaddai.flashcards.model.DeckType.LIBRARIES
import com.saishaddai.flashcards.model.DeckType.NAVIGATION
import com.saishaddai.flashcards.model.DeckType.SECURITY
import com.saishaddai.flashcards.model.DeckType.TESTING
import com.saishaddai.flashcards.model.Flashcard
import com.saishaddai.flashcards.model.fcdata.databaseCards
import com.saishaddai.flashcards.repository.FlashcardRepository
import com.saishaddai.flashcards.utils.random
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json

class JSONFlashcardRepository(private val context: Context? = null) :
    FlashcardRepository<DeckType, Flashcard> {

    val countMap: MutableMap<Int, Int> = mutableMapOf()

    override suspend fun getData(type: DeckType, size: Int): List<Flashcard> =
        withContext(Dispatchers.IO) {
            getFlashcardsForDeck(type.id).random(size)
        }

    override suspend fun getDataCount(type: DeckType): Int {
        return countMap[type.id] ?: getFlashcardsForDeck(type.id).size.also {
            countMap[type.id] = it
        }
    }

    fun getFlashcardsForDeck(deckId: Int): List<Flashcard> {
        return when (deckId) {
            OOP.id -> getListFromJson(context, OOP)
            ANDROID_CORE.id -> getListFromJson(context, ANDROID_CORE)
            KOTLIN.id -> getListFromJson(context, KOTLIN)
            KOTLIN_MP.id -> getListFromJson(context, KOTLIN_MP)
            SECURITY.id -> getListFromJson(context, SECURITY)
            COMPOSE.id -> getListFromJson(context, COMPOSE)
            DeckType.DATABASES.id -> databaseCards.storeCount(DeckType.DATABASES.id)
            DI.id -> getListFromJson(context, DI)
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
            GRAPHQL.id -> getListFromJson(context, GRAPHQL)
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