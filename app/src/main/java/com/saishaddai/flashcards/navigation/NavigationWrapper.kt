package com.saishaddai.flashcards.navigation

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
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
import com.saishaddai.flashcards.utils.navigateBack
import com.saishaddai.flashcards.utils.navigateTo
import com.saishaddai.flashcards.utils.resetTo

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
                    DeckListScreen(
                        onStartSessionClick = { deck -> backStack.navigateTo(FlashcardSession(deck)) }
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
                    FlashcardScreen(
                        onCancelSessionClick = { backStack.navigateBack() },
                        onFinishedSessionClick = { reviewed, start, end ->
                            backStack.navigateTo(FinishSession(route.deck, reviewed, start, end))
                        },
                        deck = route.deck
                    )
                }
                entry<Instructions> {
                    InstructionsScreen(
                        onPromoClick = { deck -> backStack.navigateTo(FlashcardSession(deck)) }
                    )
                }
                entry<Stats> {
                    StatsScreen(
                        onPromoClick = { deck -> backStack.navigateTo(FlashcardSession(deck)) }
                    )
                }
                entry<Settings> {
                    SettingsScreen()
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
