package com.saishaddai.flashcards.utils

import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey

fun NavBackStack<NavKey>.navigateTo(route: NavKey) = add(route)

fun NavBackStack<NavKey>.navigateBack() = removeLastOrNull()

fun NavBackStack<NavKey>.resetTo(route: NavKey) {
    clear()
    add(route)
}
