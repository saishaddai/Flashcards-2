package com.saishaddai.flashcards.data.assets

import android.content.Context
import com.saishaddai.flashcards.model.DeckType
import com.saishaddai.flashcards.model.Flashcard
import kotlinx.serialization.json.Json
import timber.log.Timber

interface FlashcardAssetDataSource {
    suspend fun loadFlashcardsForDeck(deckType: DeckType): List<Flashcard>
}

class FlashcardAssetDataSourceImpl(private val context: Context) : FlashcardAssetDataSource {
    private val json = Json {
        ignoreUnknownKeys = true
        coerceInputValues = true
    }

    override suspend fun loadFlashcardsForDeck(deckType: DeckType): List<Flashcard> {
        return try {
            val fileName = deckType.jsonFile
            if (fileName.isEmpty()) return emptyList()

            context.assets.open("decks/$fileName")
                .bufferedReader()
                .use { it.readText() }
                .let { json.decodeFromString<List<Flashcard>>(it) }
        } catch (e: Exception) {
            Timber.e(e, "Error loading flashcards for ${deckType.name} from ${deckType.jsonFile}")
            emptyList()
        }
    }
}
