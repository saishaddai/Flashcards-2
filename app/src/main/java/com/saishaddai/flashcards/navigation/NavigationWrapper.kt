package com.saishaddai.flashcards.navigation

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import com.saishaddai.flashcards.routes.Route
import com.saishaddai.flashcards.routes.Route.DeckList
import com.saishaddai.flashcards.routes.Route.FinishSession
import com.saishaddai.flashcards.routes.Route.FlashcardSession
import com.saishaddai.flashcards.routes.Route.Instructions
import com.saishaddai.flashcards.routes.Route.Settings
import com.saishaddai.flashcards.routes.Route.Stats
import com.saishaddai.flashcards.screens.DeckListScreen
import com.saishaddai.flashcards.screens.FinishSessionScreen
import com.saishaddai.flashcards.screens.FlashcardScreen
import com.saishaddai.flashcards.screens.InstructionsScreen
import com.saishaddai.flashcards.screens.SettingsScreen
import com.saishaddai.flashcards.screens.StatsScreen
import com.saishaddai.flashcards.utils.UiState
import com.saishaddai.flashcards.utils.navigateBack
import com.saishaddai.flashcards.utils.navigateTo
import com.saishaddai.flashcards.utils.resetTo

import com.saishaddai.flashcards.viewmodel.DecksViewModel
import com.saishaddai.flashcards.viewmodel.FlashcardViewModel
import com.saishaddai.flashcards.viewmodel.SettingsViewModel
import com.saishaddai.flashcards.viewmodel.StatsViewModel
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun NavigationWrapper() {
    val backStack: NavBackStack<NavKey> = rememberNavBackStack(DeckList)
    val currentKey = backStack.lastOrNull() as? Route

    Scaffold(
        bottomBar = {
            if (currentKey is DeckList || currentKey is Instructions || currentKey is Stats || currentKey is Settings) {
                MainBottomNavigation(
                    currentRoute = currentKey,
                    onLearnClick = { if (currentKey !is DeckList) backStack.navigateTo(DeckList) },
                    onInstructionsClick = {
                        if (currentKey !is Instructions) backStack.navigateTo(Instructions)
                    },
                    onStatsClick = { if (currentKey !is Stats) backStack.navigateTo(Stats) },
                    onSettingsClick = { if (currentKey !is Settings) backStack.navigateTo(Settings) }
                )
            }
        }
    ) { innerPadding ->
        NavDisplay(
            modifier = Modifier.padding(innerPadding),
            backStack = backStack,
            onBack = { backStack.removeLastOrNull() },
            entryProvider = entryProvider {
                entry<DeckList> {
                    val viewModel: DecksViewModel = koinViewModel()
                    val settingsViewModel: SettingsViewModel = koinViewModel()
                    val uiState by viewModel.uiState.collectAsState()
                    val userSettings by settingsViewModel.userSettings.collectAsState()
                    val quickStartEnabled = userSettings?.quickStart ?: false

                    DeckListScreen(
                        uiState = uiState,
                        quickStartEnabled = quickStartEnabled,
                        onDeckSelected = viewModel::onDeckSelected,
                        onStartSessionClick = { deck -> backStack.navigateTo(FlashcardSession(deck)) },
                        onDismissEmptyDeckDialog = viewModel::dismissEmptyDeckDialog,
                        onTriggerEmptyDeckDialog = viewModel::onStartSession,
                        onRetry = viewModel::loadDecks
                    )
                }
                entry<FinishSession> { route ->
                    FinishSessionScreen(
                        deck = route.deck,
                        cardsReviewed = route.cardsReviewed,
                        startTime = route.startTime,
                        endTime = route.endTime,
                        onFinishSession = {
                            backStack.resetTo(DeckList)
                        },
                        onShareSummary = {}
                    )
                }
                entry<FlashcardSession> { route ->
                    val viewModel: FlashcardViewModel = koinViewModel(
                        key = route.deck.id.toString(),
                        parameters = { org.koin.core.parameter.parametersOf(route.deck.id) }
                    )
                    val uiState by viewModel.uiState.collectAsState()

                    FlashcardScreen(
                        uiState = uiState,
                        deck = route.deck,
                        onShowResponseClicked = viewModel::onShowResponseClicked,
                        onPageChanged = viewModel::onPageChanged,
                        onFinishSession = viewModel::onFinishSession,
                        onCancelSessionClick = { backStack.navigateBack() },
                        onFinishedSessionClick = {
                            val (reviewed, start, end) = viewModel.getSessionSummary()
                            backStack.navigateTo(FinishSession(route.deck, reviewed, start, end))
                        },
                        onRetry = viewModel::loadFlashcards
                    )
                }
                entry<Instructions> {
                    val decksViewModel: DecksViewModel = koinViewModel()
                    val settingsViewModel: SettingsViewModel = koinViewModel()

                    val uiState by decksViewModel.uiState.collectAsState()
                    val promoDeck = remember(uiState) {
                        (uiState as? UiState.Success)?.data?.decks?.randomOrNull()
                    }
                    val settingsUiState by settingsViewModel.uiState.collectAsState()
                    val showSuggestions = (settingsUiState as? UiState.Success)?.data?.userSettings?.showSuggestions ?: true

                    InstructionsScreen(
                        promoDeck = promoDeck,
                        showSuggestions = showSuggestions,
                        onPromoClick = { deck -> backStack.navigateTo(FlashcardSession(deck)) }
                    )
                }
                entry<Stats> {
                    val statsViewModel: StatsViewModel = koinViewModel()
                    val decksViewModel: DecksViewModel = koinViewModel()
                    val settingsViewModel: SettingsViewModel = koinViewModel()

                    val uiState by statsViewModel.uiState.collectAsState()
                    val promoDeck = remember(uiState) {
                        decksViewModel.getRandomDeck()
                    }
                    val settingsUiState by settingsViewModel.uiState.collectAsState()
                    val showSuggestions = (settingsUiState as? UiState.Success)?.data?.userSettings?.showSuggestions ?: true

                    StatsScreen(
                        uiState = uiState,
                        promoDeck = promoDeck,
                        showSuggestions = showSuggestions,
                        onViewAllSkillsClicked = statsViewModel::onViewAllSkillsClicked,
                        onInfoClick = statsViewModel::onInfoClick,
                        onDismissInfoDialog = statsViewModel::onDismissInfoDialog,
                        onRetry = statsViewModel::loadStats,
                        onPromoClick = { deck -> backStack.navigateTo(FlashcardSession(deck)) }
                    )
                }
                entry<Settings> {
                    val viewModel: SettingsViewModel = koinViewModel()
                    val uiState by viewModel.uiState.collectAsState()

                    SettingsScreen(
                        uiState = uiState,
                        onRestartMasteryClicked = viewModel::onRestartMasteryClicked,
                        onPreferredStudyTimeChanged = viewModel::onPreferredStudyTimeChanged,
                        onFlashcardsPerSessionChanged = viewModel::onFlashcardsPerSessionChanged,
                        onDailyStudyGoalChanged = viewModel::onDailyStudyGoalChanged,
                        onQuickStartChanged = viewModel::onQuickStartChanged,
                        onShowAnswersChanged = viewModel::onShowAnswersChanged,
                        onShowSuggestionsChanged = viewModel::onShowSuggestionsChanged,
                        onStudyRemindersChanged = viewModel::onStudyRemindersChanged,
                        onNotificationSoundChanged = viewModel::onNotificationSoundChanged,
                        onRetry = { /* Settings load is automatic with flow */ }
                    )
                }
            },
            transitionSpec = {
                slideInHorizontally(
                    initialOffsetX = { it },
                    animationSpec = tween(1000)
                ) togetherWith slideOutHorizontally(
                    targetOffsetX = { -it },
                    animationSpec = tween(1000)
                )
            },
            popTransitionSpec = {
                slideInHorizontally(
                    initialOffsetX = { -it },
                    animationSpec = tween(500)
                ) togetherWith slideOutHorizontally(
                    targetOffsetX = { it },
                    animationSpec = tween(500)
                )
            },
            predictivePopTransitionSpec = {
                slideInHorizontally(
                    initialOffsetX = { -it },
                    animationSpec = tween(500)
                ) togetherWith slideOutHorizontally(
                    targetOffsetX = { it },
                    animationSpec = tween(500)
                )
            }
        )
    }
}
