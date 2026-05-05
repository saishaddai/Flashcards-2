package com.saishaddai.flashcards.repository.impl

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock

@OptIn(ExperimentalCoroutinesApi::class)
class DataStoreSettingsRepositoryTest {

    @get:Rule
    val tmpFolder = TemporaryFolder()

    private val testDispatcher = StandardTestDispatcher()
    private val testScope = TestScope(testDispatcher + Job())

    private lateinit var dataStore: DataStore<Preferences>
    private lateinit var repository: DataStoreSettingsRepository
    private lateinit var context: Context

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)

        context = mock {
            on { applicationContext } doReturn it
        }

        dataStore = PreferenceDataStoreFactory.create(
            scope = testScope,
            produceFile = { tmpFolder.newFile("test.preferences_pb") }
        )

        // We need to inject the DataStore into the repository.
        // But the current implementation uses the extension property Context.dataStore.
        // To make it testable, we'll need to refactor DataStoreSettingsRepository slightly
        // to accept DataStore as a dependency.
        repository = DataStoreSettingsRepository(context, dataStore)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        testScope.cancel()
    }

    @Test
    fun `initial settings are default`() = runTest(testDispatcher) {
        val settings = repository.getSettings().first()
        assertEquals(20, settings.flashcardsPerSession)
        assertEquals(50, settings.dailyStudyGoal)
        assertEquals(true, settings.isDarkMode)
        assertEquals("09:00 PM", settings.preferredStudyTime)
    }

    @Test
    fun `saveFlashcardsPerSession updates value`() = runTest(testDispatcher) {
        repository.saveFlashcardsPerSession(35)
        val settings = repository.getSettings().first()
        assertEquals(35, settings.flashcardsPerSession)
    }

    @Test
    fun `saveDailyStudyGoal updates value`() = runTest(testDispatcher) {
        repository.saveDailyStudyGoal(100)
        val settings = repository.getSettings().first()
        assertEquals(100, settings.dailyStudyGoal)
    }

    @Test
    fun `saveDarkMode updates value`() = runTest(testDispatcher) {
        repository.saveDarkMode(false)
        val settings = repository.getSettings().first()
        assertFalse(settings.isDarkMode)
    }

    @Test
    fun `savePreferredStudyTime updates value`() = runTest(testDispatcher) {
        val newTime = "10:30 AM"
        repository.savePreferredStudyTime(newTime)
        val settings = repository.getSettings().first()
        assertEquals(newTime, settings.preferredStudyTime)
    }

    @Test
    fun `restartMasteryExperience clears values`() = runTest(testDispatcher) {
        repository.saveFlashcardsPerSession(40)
        repository.restartMasteryExperience()
        val settings = repository.getSettings().first()
        assertEquals(20, settings.flashcardsPerSession) // Should return to default
    }
}
