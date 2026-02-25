package com.saishaddai.flashcards.utils

import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey

fun NavBackStack<NavKey>.navigateTo(screen: NavKey) = add(screen)

fun NavBackStack<NavKey>.navigateBack() = removeLastOrNull()