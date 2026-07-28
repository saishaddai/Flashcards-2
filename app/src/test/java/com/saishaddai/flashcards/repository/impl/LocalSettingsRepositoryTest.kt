package com.saishaddai.flashcards.repository.impl

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import com.saishaddai.flashcards.data.local.SessionSummaryDao
import com.saishaddai.flashcards.data.local.StudyDao
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
import org.mockito.kotlin.verify

@OptIn(ExperimentalCoroutinesApi::class)
class LocalSettingsRepositoryTest {

    @get:Rule
    val tmpFolder = TemporaryFolder()

    private val testDispatcher = StandardTestDispatcher()
    private val testScope = TestScope(testDispatcher + Job())

    private lateinit var dataStore: DataStore<Preferences>
    private lateinit var repository: LocalSettingsRepository
    private lateinit var context: Context
    private val studyDao: StudyDao = mock()
    private val sessionSummaryDao: SessionSummaryDao = mock()

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

        repository = LocalSettingsRepository(context, studyDao, sessionSummaryDao, dataStore)
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
    fun `saveDarkMode updates value`() = runTest(testDispatcher) {
        repository.saveDarkMode(false)
        val settings = repository.getSettings().first()
        assertFalse(settings.isDarkMode)
    }

    @Test
    fun `restartMasteryExperience calls delete on DAOs`() = runTest(testDispatcher) {
        repository.restartMasteryExperience()
        
        verify(studyDao).deleteAllSessions()
        verify(studyDao).deleteAllDeckMastery()
        verify(studyDao).deleteAllDailyActivity()
        verify(sessionSummaryDao).deleteAllSessions()
    }
}
