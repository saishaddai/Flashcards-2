package com.saishaddai.flashcards.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.saishaddai.flashcards.repository.SettingsRepository
import com.saishaddai.flashcards.repository.HardcodedSettingsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SettingsViewModel(
    private val repository: SettingsRepository = HardcodedSettingsRepository()
) : ViewModel() {

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    fun onRestartMasteryClicked() {
        viewModelScope.launch {
            repository.restartMasteryExperience()
        }
    }

    fun onPreferredStudyTimeClicked() {
        // TODO: Implement time picker logic or navigation
    }
    
    // Additional methods for saving other settings can be added here
}
