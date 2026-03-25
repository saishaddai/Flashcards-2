package com.saishaddai.flashcards.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class FinishSessionViewModel : ViewModel() {
    private val _navigateToDeckList = MutableStateFlow(false)
    val navigateToDeckList: StateFlow<Boolean> = _navigateToDeckList.asStateFlow()

    fun onBackToDecksClicked() {
        _navigateToDeckList.value = true
    }

    fun onNavigationHandled() {
        _navigateToDeckList.value = false
    }
}
