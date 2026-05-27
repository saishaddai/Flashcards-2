package com.saishaddai.flashcards.screens

import android.app.Application
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.doubleClick
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTouchInput
import androidx.test.platform.app.InstrumentationRegistry
import com.saishaddai.flashcards.R
import com.saishaddai.flashcards.model.Deck
import com.saishaddai.flashcards.repository.DeckRepository
import com.saishaddai.flashcards.repository.UserSettings
import com.saishaddai.flashcards.utils.TestTags
import com.saishaddai.flashcards.viewmodel.DecksViewModel
import com.saishaddai.flashcards.viewmodel.SettingsViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.kotlin.whenever

class DeckListScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val context = InstrumentationRegistry.getInstrumentation().targetContext
    private val application: Application = mock()

    private fun createMockSettingsViewModel(quickStart: Boolean = false): SettingsViewModel {
        val viewModel: SettingsViewModel = mock()
        whenever(viewModel.userSettings).thenReturn(MutableStateFlow(UserSettings(quickStart = quickStart)))
        return viewModel
    }

    @Test
    fun testDeckListScreen_initialState_showsLoaderThenDecks() {
        val mockDecks = listOf(
            Deck(id = 1, name = "Mock Deck 1", longName = "Mock Deck 1 Long", cardCount = 10)
        )

        val fakeRepository = object : DeckRepository<Deck> {
            override suspend fun getData(): List<Deck> {
                kotlinx.coroutines.delay(500) // Simulate loading
                return mockDecks
            }
        }

        val viewModel = DecksViewModel(application, fakeRepository)
        val settingsViewModel = createMockSettingsViewModel()

        composeTestRule.setContent {
            DeckListScreen(
                viewModel = viewModel,
                settingsViewModel = settingsViewModel,
                onStartSessionClick = {}
            )
        }

        // Check if loader is shown initially
        composeTestRule.onNodeWithTag(TestTags.FULL_LOADER).assertIsDisplayed()

        // Wait for loader to disappear
        composeTestRule.waitUntil(5000) {
            composeTestRule.onAllNodesWithTag(TestTags.FULL_LOADER).fetchSemanticsNodes().isEmpty()
        }

        composeTestRule.onNodeWithText(context.getString(R.string.decks_welcome)).assertIsDisplayed()
        composeTestRule.onNodeWithText("Mock Deck 1").assertIsDisplayed()
    }

    @Test
    fun testDeckListScreen_displaysMockDecks() {
        val mockDecks = listOf(
            Deck(id = 1, name = "Mock Deck 1", longName = "Mock Deck 1 Long", cardCount = 10),
            Deck(id = 2, name = "Mock Deck 2", longName = "Mock Deck 2 Long", cardCount = 5)
        )

        val fakeRepository = object : DeckRepository<Deck> {
            override suspend fun getData(): List<Deck> = mockDecks
        }

        val viewModel = DecksViewModel(application, fakeRepository)
        val settingsViewModel = createMockSettingsViewModel()

        composeTestRule.setContent {
            DeckListScreen(
                viewModel = viewModel,
                settingsViewModel = settingsViewModel,
                onStartSessionClick = {}
            )
        }

        composeTestRule.onNodeWithText("Mock Deck 1").assertIsDisplayed()
        composeTestRule.onNodeWithText("Mock Deck 2").assertIsDisplayed()
    }

    @Test
    fun testDeckListScreen_emptyMockDeck_opensDialogThatCanBeDismissed() {
        val mockDecks = listOf(
            Deck(id = 1, name = "Empty Deck", longName = "Empty Deck Long", cardCount = 0, isSelected = true)
        )

        val fakeRepository = object : DeckRepository<Deck> {
            override suspend fun getData(): List<Deck> = mockDecks
        }

        val viewModel = DecksViewModel(application, fakeRepository)
        val settingsViewModel = createMockSettingsViewModel()

        composeTestRule.setContent {
            DeckListScreen(
                viewModel = viewModel,
                settingsViewModel = settingsViewModel,
                onStartSessionClick = {}
            )
        }

        composeTestRule.onNodeWithText(context.getString(R.string.decks_start_session_button)).performClick()

        // Check dialog
        composeTestRule.onNodeWithText(context.getString(R.string.empty_deck_confirm)).assertIsDisplayed()
            .performClick().assertDoesNotExist()
    }

    @Test
    fun testDeckListScreen_clickMockDeck_triggersCallBack() {
        var onStartSessionClickCalled = false
        val mockDecks = listOf(
            Deck(id = 1, name = "Mock Deck", longName = "Mock Deck Long", cardCount = 1, isSelected = true)
        )

        val fakeRepository = object : DeckRepository<Deck> {
            override suspend fun getData(): List<Deck> = mockDecks
        }

        val viewModel = DecksViewModel(application, fakeRepository)
        val settingsViewModel = createMockSettingsViewModel()

        composeTestRule.setContent {
            DeckListScreen(
                viewModel = viewModel,
                settingsViewModel = settingsViewModel,
                onStartSessionClick = { onStartSessionClickCalled = true }
            )
        }

        composeTestRule.onNodeWithText(context.getString(R.string.decks_start_session_button)).performClick()

        composeTestRule.waitUntil(5000) { onStartSessionClickCalled }
    }

    @Test
    fun testDeckListScreen_deckCount_displaysCorrectText() {
        val mockDecks = listOf(
            Deck(id = 1, name = "Regular Deck", longName = "Regular", cardCount = 50, mastery = 20),
            Deck(id = 2, name = "Large Deck", longName = "Large", cardCount = 150, mastery = 80)
        )

        val fakeRepository = object : DeckRepository<Deck> {
            override suspend fun getData(): List<Deck> = mockDecks
        }

        val viewModel = DecksViewModel(application, fakeRepository)
        val settingsViewModel = createMockSettingsViewModel()

        composeTestRule.setContent {
            DeckListScreen(
                viewModel = viewModel,
                settingsViewModel = settingsViewModel,
                onStartSessionClick = {}
            )
        }

        // Regular case: 50 Cards • 20%
        val expectedRegularText = context.getString(R.string.decks_card_count, 50, 20)
        composeTestRule.onNodeWithText(expectedRegularText).assertIsDisplayed()

        // 99+ case: 99+ Cards • 80%
        val expectedLargeText = context.getString(R.string.decks_card_count_plus, 80)
        composeTestRule.onNodeWithText(expectedLargeText).assertIsDisplayed()
    }

    @Test
    fun testDeckListScreen_deckTitle_isDisplayedAndHasCorrectText() {
        val longTitle = "A very long deck title that should definitely be ellipsized"
        val mockDecks = listOf(
            Deck(id = 1, name = longTitle, longName = "Long", cardCount = 10)
        )

        val fakeRepository = object : DeckRepository<Deck> {
            override suspend fun getData(): List<Deck> = mockDecks
        }

        val viewModel = DecksViewModel(application, fakeRepository)
        val settingsViewModel = createMockSettingsViewModel()

        composeTestRule.setContent {
            DeckListScreen(
                viewModel = viewModel,
                settingsViewModel = settingsViewModel,
                onStartSessionClick = {}
            )
        }

        // Check by text directly as it should be in the hierarchy even if ellipsized visually
        composeTestRule.onNodeWithText(longTitle).assertIsDisplayed()
    }

    @Test
    fun testDeckListScreen_doubleClickMockDeck_showsConfirmationDialog() {
        val mockDecks = listOf(
            Deck(id = 1, name = "Double Click Deck", longName = "Double Click", cardCount = 10)
        )

        val fakeRepository = object : DeckRepository<Deck> {
            override suspend fun getData(): List<Deck> = mockDecks
        }

        val viewModel = DecksViewModel(application, fakeRepository)
        val settingsViewModel = createMockSettingsViewModel(quickStart = false)

        composeTestRule.setContent {
            DeckListScreen(
                viewModel = viewModel,
                settingsViewModel = settingsViewModel,
                onStartSessionClick = {}
            )
        }

        // Double click the deck title
        composeTestRule.onNodeWithText("Double Click Deck").performTouchInput {
            doubleClick()
        }

        // Verify the confirmation dialog is displayed
        composeTestRule.onNodeWithText(context.getString(R.string.flashcard_start_dialog_title)).assertIsDisplayed()
        composeTestRule.onNodeWithText(context.getString(R.string.flashcard_start_dialog_confirm)).assertIsDisplayed()
    }

    @Test
    fun testDeckListScreen_doubleClickMockDeck_quickStartEnabled_skipsDialog() {
        var onStartSessionClickCalled = false
        val mockDecks = listOf(
            Deck(id = 1, name = "Quick Start Deck", longName = "Quick Start", cardCount = 10)
        )

        val fakeRepository = object : DeckRepository<Deck> {
            override suspend fun getData(): List<Deck> = mockDecks
        }

        val viewModel = DecksViewModel(application, fakeRepository)
        val settingsViewModel = createMockSettingsViewModel(quickStart = true)

        composeTestRule.setContent {
            DeckListScreen(
                viewModel = viewModel,
                settingsViewModel = settingsViewModel,
                onStartSessionClick = { onStartSessionClickCalled = true }
            )
        }

        // Double click the deck title
        composeTestRule.onNodeWithText("Quick Start Deck").performTouchInput {
            doubleClick()
        }

        // Verify the confirmation dialog is NOT displayed
        composeTestRule.onNodeWithText(context.getString(R.string.flashcard_start_dialog_title)).assertDoesNotExist()
        
        // Verify callback was triggered immediately
        composeTestRule.waitUntil(5000) { onStartSessionClickCalled }
    }
}
