package com.saishaddai.flashcards.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.saishaddai.flashcards.model.SessionSummary
import kotlinx.coroutines.flow.Flow

@Dao
interface SessionSummaryDao {
    @Query("SELECT * FROM session_summaries")
    fun getAllSessions(): Flow<List<SessionSummary>>

    @Query("SELECT * FROM session_summaries WHERE deckId = :deckId")
    suspend fun getSessionByDeckId(deckId: Int): SessionSummary?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSession(session: SessionSummary): Long

    @Update
    suspend fun updateSession(session: SessionSummary)

    @Delete
    suspend fun deleteSession(session: SessionSummary)

    @Query("DELETE FROM session_summaries")
    suspend fun deleteAllSessions()
}
