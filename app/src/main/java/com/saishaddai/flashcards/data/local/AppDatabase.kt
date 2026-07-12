package com.saishaddai.flashcards.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.saishaddai.flashcards.model.DailyActivity
import com.saishaddai.flashcards.model.DeckMastery
import com.saishaddai.flashcards.model.Flashcard
import com.saishaddai.flashcards.model.SessionSummary
import com.saishaddai.flashcards.model.StudySession

@Database(
    entities = [
        SessionSummary::class,
        StudySession::class,
        DeckMastery::class,
        DailyActivity::class,
        Flashcard::class
    ],
    version = 4,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun sessionSummaryDao(): SessionSummaryDao
    abstract fun studyDao(): StudyDao
    abstract fun flashcardDao(): FlashcardDao
}
