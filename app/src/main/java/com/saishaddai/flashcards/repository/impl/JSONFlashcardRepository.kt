package com.saishaddai.flashcards.repository.impl

import android.content.Context
import com.saishaddai.flashcards.model.DeckType
import com.saishaddai.flashcards.model.Flashcard
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
            getFlashcardsForDeck(type).random(size)
        }

    override suspend fun getDataCount(type: DeckType): Int {
        return countMap[type.id] ?: getFlashcardsForDeck(type).size
            .also {
                countMap[type.id] = it
            }
    }

    fun getFlashcardsForDeck(deckType: DeckType): List<Flashcard> =
        getListFromJson(context, deckType)

    private fun getListFromJson(context: Context?, deckType: DeckType): List<Flashcard> {
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