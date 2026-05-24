package com.example.fintrack.presentation.screens.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fintrack.data.local.datastore.UserPreferences
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class SettingsViewModel(
    private val userPreferences: UserPreferences
) : ViewModel() {

    val isDarkMode: StateFlow<Boolean> = userPreferences.isDarkMode
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = false
        )

    val sortBy: StateFlow<String> = userPreferences.sortBy
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = "UPDATED_DESC"
        )

    val defaultCategory: StateFlow<String> = userPreferences.defaultCategory
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = "GENERAL"
        )

    val showPreview: StateFlow<Boolean> = userPreferences.showPreview
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = true
        )

    fun setDarkMode(enabled: Boolean) {
        viewModelScope.launch {
            userPreferences.setDarkMode(enabled)
        }
    }

    fun setSortBy(sortBy: String) {
        viewModelScope.launch {
            userPreferences.setSortBy(sortBy)
        }
    }

    fun setDefaultCategory(category: String) {
        viewModelScope.launch {
            userPreferences.setDefaultCategory(category)
        }
    }

    fun setShowPreview(show: Boolean) {
        viewModelScope.launch {
            userPreferences.setShowPreview(show)
        }
    }
}
