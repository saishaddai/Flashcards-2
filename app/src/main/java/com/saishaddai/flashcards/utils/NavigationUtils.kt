package com.saishaddai.flashcards.utils

import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import com.saishaddai.flashcards.routes.Route

fun NavBackStack<NavKey>.navigateTo(route: Route) = add(route)

fun NavBackStack<NavKey>.navigateBack() = removeLastOrNull()

fun NavBackStack<NavKey>.resetTo(route: Route) {
    clear()
    add(route)
}
