package com.saishaddai.flashcards.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "daily_activity")
data class DailyActivity(
    @PrimaryKey val date: String, // Format: "yyyy-MM-dd"
    val cardsReviewed: Int,
    val isGoalMet: Boolean
)
