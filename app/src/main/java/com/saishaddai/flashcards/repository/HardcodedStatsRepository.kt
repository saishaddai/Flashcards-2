package com.saishaddai.flashcards.repository

import com.saishaddai.flashcards.screens.MasteryData
import com.saishaddai.flashcards.ui.theme.RoyalBlue
import androidx.compose.ui.graphics.Color
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class HardcodedStatsRepository : StatsRepository {
    override fun getWeeklyActivity(): Flow<Int> = flow { emit(248) }
    
    override fun getSkillMastery(): Flow<List<MasteryData>> = flow {
        emit(listOf(
            MasteryData("Compose", 92, "EXPERT", RoyalBlue),
            MasteryData("Android SDK", 85, "ADVANCED", Color(0xFF10B981))
        ))
    }
    
    override fun getCardsReviewed(): Flow<String> = flow { emit("1,240") }
    override fun getCurrentStreak(): Flow<String> = flow { emit("12 Days") }
    override fun getStudyTime(): Flow<String> = flow { emit("14.5h") }
    override fun getAccuracyRate(): Flow<String> = flow { emit("88%") }
}
