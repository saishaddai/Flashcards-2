package com.saishaddai.flashcards.repository.impl

import com.saishaddai.flashcards.data.local.StudyDao
import com.saishaddai.flashcards.model.DeckType
import com.saishaddai.flashcards.model.MasteryLevel
import com.saishaddai.flashcards.repository.StatsRepository
import com.saishaddai.flashcards.screens.MasteryData
import com.saishaddai.flashcards.ui.theme.*
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
            val calendar = Calendar.getInstance()
            calendar.firstDayOfWeek = Calendar.MONDAY
            calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY)
            
            val currentWeekDays = (0..6).map { _ ->
                val date = dateFormatter.format(calendar.time)
                calendar.add(Calendar.DAY_OF_YEAR, 1)
                date
            }
            
            currentWeekDays.map { date ->
                activities.find { it.date == date }?.cardsReviewed ?: 0
            }
        }
    }

    override fun getSkillMastery(): Flow<List<MasteryData>> {
        return studyDao.getAllDeckMastery().map { masteries ->
            // Use DeckType.values() for better compatibility across Kotlin versions
            val allDecks = DeckType.values().map { type ->
                val mastery = masteries.find { it.deckId == type.id }
                if (mastery != null) {
                    val masteryLevel = MasteryLevel.fromProgress(mastery.progress.toInt())
                    MasteryData(
                        title = mastery.deckName,
                        percentage = mastery.progress.toInt(),
                        levelRes = masteryLevel.nameRes,
                        color = masteryLevel.color
                    )
                } else {
                    // Manual mapping for friendly names
                    val friendlyName = when(type) {
                        DeckType.OOP -> "OOP"
                        DeckType.ANDROID_CORE -> "Android Core"
                        DeckType.KOTLIN -> "Kotlin"
                        DeckType.KOTLIN_MP -> "Kotlin MP"
                        DeckType.SECURITY -> "Security"
                        DeckType.COMPOSE -> "Compose"
                        DeckType.DATABASES -> "Databases"
                        DeckType.DI -> "DI"
                        DeckType.MATERIAL_3 -> "Material 3"
                        DeckType.NAVIGATION -> "Navigation"
                        DeckType.JETPACK -> "Jetpack"
                        DeckType.TESTING -> "Testing"
                        DeckType.GRADLE -> "Gradle"
                        DeckType.ANDROID_OPS -> "Android OPS"
                        DeckType.LIBRARIES -> "Libraries"
                        DeckType.DESIGN_PATTERNS -> "Design Patterns"
                        DeckType.COROUTINES -> "Coroutines"
                        DeckType.FIREBASE -> "Firebase"
                        DeckType.GRAPHQL -> "GraphQL"
                        DeckType.SENSORS -> "Sensors"
                    }
                    val masteryLevel = MasteryLevel.NOT_STARTED
                    MasteryData(
                        title = friendlyName,
                        percentage = 0,
                        levelRes = masteryLevel.nameRes,
                        color = masteryLevel.color
                    )
                }
            }

            allDecks.sortedByDescending { it.percentage }
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

    override fun getWeeklyComparison(): Flow<Int> {
        return studyDao.getRecentActivity().map { activities ->
            val calendar = Calendar.getInstance()
            calendar.firstDayOfWeek = Calendar.MONDAY
            
            // Current week (Monday to Sunday)
            val currentWeekCalendar = calendar.clone() as Calendar
            currentWeekCalendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY)
            val currentWeekDates = (0..6).map {
                val date = dateFormatter.format(currentWeekCalendar.time)
                currentWeekCalendar.add(Calendar.DAY_OF_YEAR, 1)
                date
            }
            
            // Previous week (Monday to Sunday)
            val previousWeekCalendar = calendar.clone() as Calendar
            previousWeekCalendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY)
            previousWeekCalendar.add(Calendar.DAY_OF_YEAR, -7)
            val previousWeekDates = (0..6).map {
                val date = dateFormatter.format(previousWeekCalendar.time)
                previousWeekCalendar.add(Calendar.DAY_OF_YEAR, 1)
                date
            }
            
            val activityMap = activities.associateBy { it.date }
            val currentWeekTotal = currentWeekDates.sumOf { activityMap[it]?.cardsReviewed ?: 0 }
            val previousWeekTotal = previousWeekDates.sumOf { activityMap[it]?.cardsReviewed ?: 0 }
            
            if (previousWeekTotal == 0) {
                if (currentWeekTotal > 0) 100 else 0
            } else {
                ((currentWeekTotal - previousWeekTotal).toDouble() / previousWeekTotal * 100).toInt()
            }
        }
    }
}
