package com.saishaddai.flashcards.viewmodel

import com.saishaddai.flashcards.repository.SettingsRepository
import com.saishaddai.flashcards.repository.UserSettings
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class SettingsViewModelTest {
    private val testDispatcher = StandardTestDispatcher()

    private lateinit var viewModel: SettingsViewModel
    private lateinit var repository: FakeSettingsRepository

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        repository = FakeSettingsRepository()
        viewModel = SettingsViewModel(repository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `userSettings flow provides data from repository`() = runTest {
        val settings = viewModel.userSettings.first { it != null }
        assertNotNull(settings)
        assertEquals(20, settings?.flashcardsPerSession)
    }

    @Test
    fun `onRestartMasteryClicked calls repository`() = runTest {
        viewModel.onRestartMasteryClicked()
        advanceUntilIdle()
        assertTrue(repository.restartCalled)
    }

    @Test
    fun `onPreferredStudyTimeChanged updates repository with formatted time`() = runTest {
        viewModel.onPreferredStudyTimeChanged(14, 30) // 2:30 PM
        advanceUntilIdle()
        assertEquals("02:30 PM", repository.settings.value.preferredStudyTime)

        viewModel.onPreferredStudyTimeChanged(9, 5) // 9:05 AM
        advanceUntilIdle()
        assertEquals("09:05 AM", repository.settings.value.preferredStudyTime)
        
        viewModel.onPreferredStudyTimeChanged(0, 0) // 12:00 AM
        advanceUntilIdle()
        assertEquals("12:00 AM", repository.settings.value.preferredStudyTime)

        viewModel.onPreferredStudyTimeChanged(12, 0) // 12:00 PM
        advanceUntilIdle()
        assertEquals("12:00 PM", repository.settings.value.preferredStudyTime)
    }

    @Test
    fun `onFlashcardsPerSessionChanged updates repository`() = runTest {
        viewModel.onFlashcardsPerSessionChanged(30)
        advanceUntilIdle()
        assertEquals(30, repository.settings.value.flashcardsPerSession)
    }

    @Test
    fun `onDailyStudyGoalChanged updates repository`() = runTest {
        viewModel.onDailyStudyGoalChanged(75)
        advanceUntilIdle()
        assertEquals(75, repository.settings.value.dailyStudyGoal)
    }

    @Test
    fun `onQuickStartChanged updates repository`() = runTest {
        viewModel.onQuickStartChanged(false)
        advanceUntilIdle()
        assertEquals(false, repository.settings.value.quickStart)
    }

    @Test
    fun `onShowAnswersChanged updates repository`() = runTest {
        viewModel.onShowAnswersChanged(false)
        advanceUntilIdle()
        assertEquals(false, repository.settings.value.showAnswers)
    }

    @Test
    fun `onShowSuggestionsChanged updates repository`() = runTest {
        viewModel.onShowSuggestionsChanged(false)
        advanceUntilIdle()
        assertEquals(false, repository.settings.value.showSuggestions)
    }

    @Test
    fun `onStudyRemindersChanged updates repository`() = runTest {
        viewModel.onStudyRemindersChanged(false)
        advanceUntilIdle()
        assertEquals(false, repository.settings.value.studyReminders)
    }

    @Test
    fun `onNotificationSoundChanged updates repository`() = runTest {
        viewModel.onNotificationSoundChanged(true)
        advanceUntilIdle()
        assertEquals(true, repository.settings.value.notificationSound)
    }

    private class FakeSettingsRepository : SettingsRepository {
        var restartCalled = false
        val settings = MutableStateFlow(
            UserSettings(
                flashcardsPerSession = 20,
                dailyStudyGoal = 50,
                isDarkMode = true,
                studyReminders = true,
                notificationSound = false,
                preferredStudyTime = "09:00 PM",
                quickStart = true,
                showAnswers = true,
                showSuggestions = true
            )
        )

        override suspend fun restartMasteryExperience() {
            restartCalled = true
        }
        override suspend fun saveFlashcardsPerSession(count: Int) {
            settings.value = settings.value.copy(flashcardsPerSession = count)
        }
        override suspend fun saveDailyStudyGoal(count: Int) {
            settings.value = settings.value.copy(dailyStudyGoal = count)
        }
        override suspend fun saveDarkMode(enabled: Boolean) {
            settings.value = settings.value.copy(isDarkMode = enabled)
        }
        override suspend fun saveStudyReminders(enabled: Boolean) {
            settings.value = settings.value.copy(studyReminders = enabled)
        }
        override suspend fun saveNotificationSound(enabled: Boolean) {
            settings.value = settings.value.copy(notificationSound = enabled)
        }
        override suspend fun savePreferredStudyTime(time: String) {
            settings.value = settings.value.copy(preferredStudyTime = time)
        }
        override suspend fun saveQuickStart(enabled: Boolean) {
            settings.value = settings.value.copy(quickStart = enabled)
        }
        override suspend fun saveShowAnswers(enabled: Boolean) {
            settings.value = settings.value.copy(showAnswers = enabled)
        }
        override suspend fun saveShowSuggestions(enabled: Boolean) {
            settings.value = settings.value.copy(showSuggestions = enabled)
        }
        override fun getSettings(): Flow<UserSettings> = settings
    }
}
