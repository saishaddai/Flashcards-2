package com.saishaddai.flashcards.repository.impl

import androidx.compose.ui.graphics.Color
import com.saishaddai.flashcards.data.local.StudyDao
import com.saishaddai.flashcards.model.DailyActivity
import com.saishaddai.flashcards.repository.StatsRepository
import com.saishaddai.flashcards.screens.MasteryData
import com.saishaddai.flashcards.ui.theme.RoyalBlue
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class RoomStatsRepository(
    private val studyDao: StudyDao
) : StatsRepository {

    override fun getWeeklyActivity(): Flow<List<Int>> {
        return studyDao.getRecentActivity().map { activities ->
            val last7Days = (0..6).map { i ->
                LocalDate.now().minusDays(i.toLong()).toString()
            }.reversed()
            
            last7Days.map { date ->
                activities.find { it.date == date }?.cardsReviewed ?: 0
            }
        }
    }

    override fun getSkillMastery(): Flow<List<MasteryData>> {
        return studyDao.getAllDeckMastery().map { masteries ->
            masteries.map {
                MasteryData(
                    title = it.deckName,
                    percentage = it.progress.toInt(),
                    level = it.level,
                    color = when (it.level) {
                        "Novato" -> Color(0xFFB0B0B0)
                        "Intermedio" -> Color(0xFFF59E0B)
                        "Avanzado" -> Color(0xFF10B981)
                        "Experto" -> RoyalBlue
                        "Master" -> Color(0xFFFFC700)
                        else -> RoyalBlue
                    }
                )
            }
        }
    }

    override fun getFlashcardsViewed(): Flow<String> {
        // Since we want the total sum, we can use a query or aggregate in flow
        return studyDao.getRecentActivity().map { activities ->
            activities.sumOf { it.cardsReviewed }.toString()
        }
    }

    override fun getCurrentStreak(): Flow<String> {
        return studyDao.getRecentActivity().map { activities ->
            var streak = 0
            val today = LocalDate.now()
            var currentDate = today
            
            // If today's goal isn't met yet, we might still be in a streak from yesterday
            // But usually we check if the goal was met.
            
            val activityMap = activities.associateBy { it.date }
            
            while (activityMap[currentDate.toString()]?.isGoalMet == true) {
                streak++
                currentDate = currentDate.minusDays(1)
            }
            
            // If today's goal isn't met, check if yesterday's was met to continue showing the streak
            if (streak == 0) {
                currentDate = today.minusDays(1)
                while (activityMap[currentDate.toString()]?.isGoalMet == true) {
                    streak++
                    currentDate = currentDate.minusDays(1)
                }
            }
            
            "$streak Days"
        }
    }

    override fun getStudyTime(): Flow<String> {
        return studyDao.getTotalStudyTimeMillis().map { millis ->
            val totalSeconds = (millis ?: 0L) / 1000
            val hours = totalSeconds / 3600
            val minutes = (totalSeconds % 3600) / 60
            if (hours > 0) "${hours}h ${minutes}m" else "${minutes}m"
        }
    }

    override fun getMasteredDecks(): Flow<String> {
        return studyDao.getAllDeckMastery().map { masteries ->
            if (masteries.isEmpty()) return@map "0%"
            val masteredCount = masteries.count { it.progress >= 80.0 }
            val percentage = (masteredCount.toDouble() / masteries.size * 100).toInt()
            "$percentage%"
        }
    }
}
