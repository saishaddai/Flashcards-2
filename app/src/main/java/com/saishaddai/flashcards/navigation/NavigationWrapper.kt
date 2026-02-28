package com.saishaddai.flashcards.navigation

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.runtime.Composable
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import com.saishaddai.flashcards.routes.Routes.DeckList
import com.saishaddai.flashcards.routes.Routes.Error
import com.saishaddai.flashcards.routes.Routes.FlashcardList
import com.saishaddai.flashcards.routes.Routes.Instructions
import com.saishaddai.flashcards.screens.DeckListScreen
import com.saishaddai.flashcards.screens.ErrorScreen
import com.saishaddai.flashcards.screens.InstructionsScreen
import com.saishaddai.flashcards.screens.QuickListScreen
import com.saishaddai.flashcards.utils.navigateBack
import com.saishaddai.flashcards.utils.navigateTo

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun NavigationWrapper() {
    val backStack: NavBackStack<NavKey> = rememberNavBackStack(DeckList)

    NavDisplay(
        backStack = backStack,
        onBack = { backStack.removeLastOrNull() },
        entryProvider = entryProvider {
            entry<DeckList> {
                DeckListScreen(
                    navigateToScreen = { deckId -> backStack.navigateTo(FlashcardList(deckId)) },
                    navigateToInstructions = { backStack.navigateTo(Instructions) }
                )
            }
            entry<FlashcardList> { value ->
                QuickListScreen(value.deckId)
            }
            entry<Instructions> {
                InstructionsScreen(
                    onStartClick = { backStack.navigateBack() },
                    onBackClick = { backStack.navigateBack() }
                )
            }
            entry<Error> {
                ErrorScreen { backStack.navigateBack() }
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
