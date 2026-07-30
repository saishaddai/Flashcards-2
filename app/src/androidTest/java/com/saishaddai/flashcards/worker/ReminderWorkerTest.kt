package com.saishaddai.flashcards.worker

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.work.ListenableWorker
import androidx.work.testing.TestListenableWorkerBuilder
import com.saishaddai.flashcards.repository.SettingsRepository
import com.saishaddai.flashcards.repository.UserSettings
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.dsl.module
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

class ReminderWorkerTest {

    private lateinit var context: Context
    private val settingsRepository: SettingsRepository = mock()

    @Before
    fun setUp() {
        context = ApplicationProvider.getApplicationContext()
        
        // Stop Koin if it's already started by the Application class
        stopKoin()
        
        // Initialize Koin for the worker to use
        startKoin {
            modules(module {
                single { settingsRepository }
            })
        }
    }

    @After
    fun tearDown() {
        stopKoin()
    }

    @Test
    fun reminderWorker_whenRemindersEnabled_returnsSuccess() = runBlocking {
        // Given
        val settings = UserSettings(studyReminders = true)
        whenever(settingsRepository.getSettings()) doReturn flowOf(settings)

        val worker = TestListenableWorkerBuilder<ReminderWorker>(context).build()

        // When
        val result = worker.doWork()

        // Then
        assertEquals(ListenableWorker.Result.success(), result)
    }

    @Test
    fun reminderWorker_whenRemindersDisabled_returnsSuccess() = runBlocking {
        // Given
        val settings = UserSettings(studyReminders = false)
        whenever(settingsRepository.getSettings()) doReturn flowOf(settings)

        val worker = TestListenableWorkerBuilder<ReminderWorker>(context).build()

        // When
        val result = worker.doWork()

        // Then
        assertEquals(ListenableWorker.Result.success(), result)
    }
}
