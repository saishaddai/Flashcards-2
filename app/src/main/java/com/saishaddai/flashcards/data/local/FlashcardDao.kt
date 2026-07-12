package com.saishaddai.flashcards.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.saishaddai.flashcards.model.Flashcard

@Dao
interface FlashcardDao {
    @Transaction
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(flashcards: List<Flashcard>)

    @Query("SELECT * FROM flashcards WHERE deckId = :deckId ORDER BY RANDOM() LIMIT :limit")
    suspend fun getFlashcardsForDeck(deckId: Int, limit: Int): List<Flashcard>

    @Query("SELECT COUNT(*) FROM flashcards WHERE deckId = :deckId")
    suspend fun getFlashcardCountForDeck(deckId: Int): Int

    @Query("SELECT COUNT(*) FROM flashcards")
    suspend fun getTotalFlashcardCount(): Int
}
