package com.saishaddai.flashcards.utils

import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey

fun <T : NavKey> NavBackStack<T>.navigateTo(route: T) = add(route)

fun <T : NavKey> NavBackStack<T>.navigateBack() = removeLastOrNull()

fun <T : NavKey> NavBackStack<T>.resetTo(route: T) {
    clear()
    add(route)
}
