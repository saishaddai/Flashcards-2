package com.saishaddai.flashcards.data.local

import androidx.room.*
import com.saishaddai.flashcards.model.DailyActivity
import com.saishaddai.flashcards.model.DeckMastery
import com.saishaddai.flashcards.model.StudySession
import kotlinx.coroutines.flow.Flow

@Dao
interface StudyDao {
    // Deck Mastery
    @Query("SELECT * FROM deck_mastery")
    fun getAllDeckMastery(): Flow<List<DeckMastery>>

    @Query("SELECT * FROM deck_mastery WHERE deckId = :deckId")
    suspend fun getDeckMastery(deckId: Int): DeckMastery?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDeckMastery(deckMastery: DeckMastery)

    // Study Sessions
    @Insert
    suspend fun insertSession(session: StudySession)

    @Query("SELECT * FROM study_sessions WHERE deckId = :deckId ORDER BY endTime DESC")
    fun getSessionsForDeck(deckId: Int): Flow<List<StudySession>>

    @Query("SELECT SUM(cardsReviewed) FROM study_sessions WHERE endTime >= :since")
    fun getTotalCardsReviewedSince(since: Long): Flow<Int?>

    // Daily Activity
    @Query("SELECT * FROM daily_activity WHERE date = :date")
    suspend fun getDailyActivity(date: String): DailyActivity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDailyActivity(activity: DailyActivity)

    @Query("SELECT * FROM daily_activity ORDER BY date DESC LIMIT 30")
    fun getRecentActivity(): Flow<List<DailyActivity>>

    @Query("SELECT SUM(durationMillis) FROM study_sessions")
    fun getTotalStudyTimeMillis(): Flow<Long?>

    @Transaction
    suspend fun completeSession(
        session: StudySession,
        deckMastery: DeckMastery,
        dailyActivity: DailyActivity
    ) {
        insertSession(session)
        insertDeckMastery(deckMastery)
        insertDailyActivity(dailyActivity)
    }

    @Query("DELETE FROM study_sessions")
    suspend fun deleteAllSessions()

    @Query("DELETE FROM deck_mastery")
    suspend fun deleteAllDeckMastery()

    @Query("DELETE FROM daily_activity")
    suspend fun deleteAllDailyActivity()
}
