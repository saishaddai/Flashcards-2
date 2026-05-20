package com.saishaddai.flashcards.repository.impl

import androidx.compose.ui.graphics.Color
import com.saishaddai.flashcards.data.local.StudyDao
import com.saishaddai.flashcards.repository.StatsRepository
import com.saishaddai.flashcards.screens.MasteryData
import com.saishaddai.flashcards.ui.theme.RoyalBlue
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class RoomStatsRepository(
    private val studyDao: StudyDao
) : StatsRepository {

    private val dateFormatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

    override fun getWeeklyActivity(): Flow<List<Int>> {
        return studyDao.getRecentActivity().map { activities ->
            val last7Days = (0..6).map { i ->
                val calendar = Calendar.getInstance()
                calendar.add(Calendar.DAY_OF_YEAR, -i)
                dateFormatter.format(calendar.time)
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
        return studyDao.getRecentActivity().map { activities ->
            activities.sumOf { it.cardsReviewed }.toString()
        }
    }

    override fun getCurrentStreak(): Flow<String> {
        return studyDao.getRecentActivity().map { activities ->
            var streak = 0
            val calendar = Calendar.getInstance()
            
            val activityMap = activities.associateBy { it.date }
            
            // Check today
            var currentDateStr = dateFormatter.format(calendar.time)
            while (activityMap[currentDateStr]?.isGoalMet == true) {
                streak++
                calendar.add(Calendar.DAY_OF_YEAR, -1)
                currentDateStr = dateFormatter.format(calendar.time)
            }
            
            // If today's goal isn't met, check if yesterday's was met to continue showing the streak
            if (streak == 0) {
                val yesterday = Calendar.getInstance()
                yesterday.add(Calendar.DAY_OF_YEAR, -1)
                var yesterdayDateStr = dateFormatter.format(yesterday.time)
                while (activityMap[yesterdayDateStr]?.isGoalMet == true) {
                    streak++
                    yesterday.add(Calendar.DAY_OF_YEAR, -1)
                    yesterdayDateStr = dateFormatter.format(yesterday.time)
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
