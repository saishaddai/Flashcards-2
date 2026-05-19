package com.saishaddai.flashcards.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "study_sessions")
data class StudySession(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val deckId: Int,
    val cardsReviewed: Int,
    val startTime: Long,
    val endTime: Long,
    val xpGained: Double,
    val streakAtTime: Int
)
