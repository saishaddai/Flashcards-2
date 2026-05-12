package com.saishaddai.flashcards.repository

import com.saishaddai.flashcards.screens.MasteryData
import kotlinx.coroutines.flow.Flow

interface StatsRepository {
    fun getWeeklyActivity(): Flow<List<Int>>
    fun getSkillMastery(): Flow<List<MasteryData>>
    fun getCardsReviewed(): Flow<String>
    fun getCurrentStreak(): Flow<String>
    fun getStudyTime(): Flow<String>
    fun getAccuracyRate(): Flow<String>
}
