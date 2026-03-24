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
import com.saishaddai.flashcards.routes.Routes.DeckList
import com.saishaddai.flashcards.routes.Routes.FinishSession
import com.saishaddai.flashcards.routes.Routes.FlashcardList
import com.saishaddai.flashcards.routes.Routes.FlashcardSession
import com.saishaddai.flashcards.routes.Routes.Instructions
import com.saishaddai.flashcards.routes.Routes.Settings
import com.saishaddai.flashcards.routes.Routes.Stats
import com.saishaddai.flashcards.screens.DeckListScreen
import com.saishaddai.flashcards.screens.FinishSessionScreen
import com.saishaddai.flashcards.screens.FlashcardScreen
import com.saishaddai.flashcards.screens.InstructionsScreen
import com.saishaddai.flashcards.screens.QuickListScreen
import com.saishaddai.flashcards.screens.SettingsScreen
import com.saishaddai.flashcards.screens.StatsScreen
import com.saishaddai.flashcards.utils.navigateBack
import com.saishaddai.flashcards.utils.navigateTo

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun NavigationWrapper() {
    val backStack: NavBackStack<NavKey> = rememberNavBackStack(DeckList)
    val currentKey = backStack.lastOrNull()

    Scaffold(
        bottomBar = {
            if (currentKey == DeckList || currentKey == Instructions || currentKey == Stats || currentKey == Settings) {
                MainBottomNavigation(
                    currentRoute = currentKey,
                    onLearnClick = { if (currentKey != DeckList) backStack.navigateTo(DeckList) },
                    onInstructionsClick = {
                        if (currentKey != Instructions) backStack.navigateTo(
                            Instructions
                        )
                    },
                    onStatsClick = { if (currentKey != Stats) backStack.navigateTo(Stats) },
                    onSettingsClick = { if (currentKey != Settings) backStack.navigateTo(Settings) }
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
                        onStartSessionClick = { deck -> backStack.navigateTo(FlashcardSession(deck)) },
                        onInstructionsClick = { backStack.navigateTo(Instructions) }
                    )
                }
                entry<FinishSession> {
                    FinishSessionScreen()
                }
                entry<FlashcardList> { route ->
                    QuickListScreen(route.deckId)
                }
                entry<FlashcardSession> { route ->
                    FlashcardScreen(
                        onCancelSessionClick = { backStack.navigateBack() },
                        onFinishedSessionClick = { backStack.navigateTo(FinishSession) },
                        deck = route.deck
                    )
                }
                entry<Instructions> {
                    InstructionsScreen(
                        onPromoClick = { deck -> backStack.navigateTo(FlashcardSession(deck)) }
                    )
                }
                entry<Stats> {
                    StatsScreen()
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
