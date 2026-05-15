package com.saishaddai.flashcards.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.saishaddai.flashcards.model.SessionSummary

@Database(entities = [SessionSummary::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun sessionSummaryDao(): SessionSummaryDao
}
