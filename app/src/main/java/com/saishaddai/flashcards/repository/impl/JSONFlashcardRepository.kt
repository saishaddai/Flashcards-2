package com.saishaddai.flashcards.repository.impl

import android.content.Context
import com.saishaddai.flashcards.model.DeckType
import com.saishaddai.flashcards.model.DeckType.OOP
import com.saishaddai.flashcards.model.DeckType.KOTLIN
import com.saishaddai.flashcards.model.DeckType.KOTLIN_MP
import com.saishaddai.flashcards.model.DeckType.FIREBASE
import com.saishaddai.flashcards.model.DeckType.SECURITY
import com.saishaddai.flashcards.model.Flashcard
import com.saishaddai.flashcards.model.fcdata.androidCards
import com.saishaddai.flashcards.model.fcdata.composeCards
import com.saishaddai.flashcards.model.fcdata.coroutinesCards
import com.saishaddai.flashcards.model.fcdata.databaseCards
import com.saishaddai.flashcards.model.fcdata.diCards
import com.saishaddai.flashcards.model.fcdata.jetpackCards
import com.saishaddai.flashcards.model.fcdata.librariesCards
import com.saishaddai.flashcards.model.fcdata.navigationCards
import com.saishaddai.flashcards.model.fcdata.patternsCards
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
            DeckType.ANDROID_CORE.id -> androidCards.storeCount(DeckType.ANDROID_CORE.id)
            KOTLIN.id -> getListFromJson(context, KOTLIN)
            KOTLIN_MP.id -> getListFromJson(context, KOTLIN_MP)
            SECURITY.id -> getListFromJson(context, SECURITY)
            DeckType.COMPOSE.id -> composeCards.storeCount(DeckType.COMPOSE.id)
            DeckType.DATABASES.id -> databaseCards.storeCount(DeckType.DATABASES.id)
            DeckType.DAGGER_HILT.id -> diCards.storeCount(DeckType.DAGGER_HILT.id)
            DeckType.MATERIAL_3.id -> emptyList<Flashcard>().storeCount(DeckType.MATERIAL_3.id)
            DeckType.NAVIGATION.id -> navigationCards.storeCount(DeckType.NAVIGATION.id)
            DeckType.JETPACK.id -> jetpackCards.storeCount(DeckType.JETPACK.id)
            DeckType.TESTING.id -> emptyList<Flashcard>().storeCount(DeckType.TESTING.id)
            DeckType.GRADLE.id -> emptyList<Flashcard>().storeCount(DeckType.GRADLE.id)
            DeckType.ANDROID_OPS.id -> emptyList<Flashcard>().storeCount(DeckType.ANDROID_OPS.id)
            DeckType.LIBRARIES.id -> librariesCards.storeCount(DeckType.LIBRARIES.id)
            DeckType.DESIGN_PATTERNS.id -> patternsCards.storeCount(DeckType.DESIGN_PATTERNS.id)
            DeckType.COROUTINES.id -> coroutinesCards.storeCount(DeckType.COROUTINES.id)
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