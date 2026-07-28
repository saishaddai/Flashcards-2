package com.saishaddai.flashcards.repository.impl

import com.saishaddai.flashcards.data.assets.FlashcardAssetDataSource
import com.saishaddai.flashcards.data.local.FlashcardDao
import com.saishaddai.flashcards.model.DeckType
import com.saishaddai.flashcards.model.Flashcard
import com.saishaddai.flashcards.repository.FlashcardRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber

class LocalFlashcardRepository(
    private val flashcardAssetDataSource: FlashcardAssetDataSource,
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
        try {
            val count = flashcardDao.getTotalFlashcardCount()
            if (count == 0) {
                Timber.d("Database empty, pre-populating from assets...")
                val allFlashcards = mutableListOf<Flashcard>()

                DeckType.entries.forEach { deckType ->
                    val deckCards = flashcardAssetDataSource.loadFlashcardsForDeck(deckType)
                    allFlashcards.addAll(deckCards)
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
