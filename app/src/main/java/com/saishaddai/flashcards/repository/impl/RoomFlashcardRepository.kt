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
        if (flashcardDao.getTotalFlashcardCount() == 0) {
            Timber.d("Database empty, pre-populating from JSON assets...")
            val allFlashcards = mutableListOf<Flashcard>()
            
            DeckType.entries.forEach { deckType ->
                try {
                    val jsonString = context.assets.open("decks/${deckType.jsonFile}")
                        .bufferedReader()
                        .use { it.readText() }
                    val deckCards = Json.decodeFromString<List<Flashcard>>(jsonString)
                    allFlashcards.addAll(deckCards)
                } catch (e: Exception) {
                    Timber.e(e, "Error loading flashcards for ${deckType.name}")
                }
            }
            
            if (allFlashcards.isNotEmpty()) {
                flashcardDao.insertAll(allFlashcards)
                Timber.d("Pre-population complete. Inserted ${allFlashcards.size} cards.")
            }
        }
    }
}
