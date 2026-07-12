package com.saishaddai.flashcards.repository.impl

import android.content.Context
import com.saishaddai.flashcards.data.local.FlashcardDao
import com.saishaddai.flashcards.model.DeckType
import com.saishaddai.flashcards.model.Flashcard
import com.saishaddai.flashcards.repository.FlashcardRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import timber.log.Timber

class RoomFlashcardRepository(
    private val context: Context,
    private val flashcardDao: FlashcardDao
) : FlashcardRepository<DeckType, Flashcard> {

    private val json = Json {
        ignoreUnknownKeys = true
        coerceInputValues = true
    }

    override suspend fun getData(type: DeckType, size: Int): List<Flashcard> =
        withContext(Dispatchers.IO) {
            ensureDataLoaded()
            flashcardDao.getFlashcardsForDeck(type.id, size)
        }

    override suspend fun getDataCount(type: DeckType): Int =
        withContext(Dispatchers.IO) {
            ensureDataLoaded()
            flashcardDao.getFlashcardCountForDeck(type.id)
        }

    private suspend fun ensureDataLoaded() {
        try {
            val count = flashcardDao.getTotalFlashcardCount()
            if (count == 0) {
                Timber.d("Database empty, pre-populating from JSON assets...")
                val allFlashcards = mutableListOf<Flashcard>()

                DeckType.entries.forEach { deckType ->
                    try {
                        val fileName = deckType.jsonFile
                        if (fileName.isEmpty()) return@forEach
                        
                        val jsonString = context.assets.open("decks/$fileName")
                            .bufferedReader()
                            .use { it.readText() }
                        val deckCards = json.decodeFromString<List<Flashcard>>(jsonString)
                        allFlashcards.addAll(deckCards)
                    } catch (e: Exception) {
                        Timber.e(e, "Error loading flashcards for ${deckType.name} from ${deckType.jsonFile}")
                    }
                }

                if (allFlashcards.isNotEmpty()) {
                    try {
                        flashcardDao.insertAll(allFlashcards)
                        Timber.d("Pre-population complete. Inserted ${allFlashcards.size} cards.")
                    } catch (e: Exception) {
                        Timber.e(e, "CRITICAL: Failed to insert flashcards into database")
                        throw RuntimeException("Database insertion failed", e)
                    }
                } else {
                    Timber.e("CRITICAL: No flashcards were loaded from assets!")
                    throw RuntimeException("No flashcards found in assets")
                }
            }
        } catch (e: Exception) {
            Timber.e(e, "Error during data population")
            throw e
        }
    }
}
