package com.saishaddai.flashcards.repository.impl

import androidx.compose.ui.graphics.Color
import com.saishaddai.flashcards.R
import com.saishaddai.flashcards.repository.StatsRepository
import com.saishaddai.flashcards.screens.MasteryData
import com.saishaddai.flashcards.ui.theme.RoyalBlue
import com.saishaddai.flashcards.ui.theme.SuccessGreen
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class HardcodedStatsRepository : StatsRepository {
    override fun getWeeklyActivity(): Flow<List<Int>> =
        flow { emit(listOf(40, 60, 20, 45, 90, 30, 50)) }

    override fun getSkillMastery(): Flow<List<MasteryData>> = flow {
        emit(
            listOf(
                MasteryData("Compose", 92, R.string.mastery_level_veteran, RoyalBlue),
                MasteryData("Android SDK", 85, R.string.mastery_level_veteran, SuccessGreen)
            )
        )
    }

    override fun getFlashcardsViewed(): Flow<String> = flow { emit("1,240") }
    override fun getCurrentStreak(): Flow<String> = flow { emit("12 Days") }
    override fun getStudyTime(): Flow<String> = flow { emit("14.5h") }
    override fun getMasteredDecks(): Flow<String> = flow { emit("88%") }
    override fun getWeeklyComparison(): Flow<Int> = flow { emit(15) }
}