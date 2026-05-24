package com.example.fintrack

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.compose.rememberNavController
import com.example.fintrack.presentation.theme.FinTrackTheme
import com.example.fintrack.presentation.navigation.AppNavHost
import com.example.fintrack.data.local.datastore.UserPreferences
import org.koin.compose.koinInject

@Composable
fun App() {
    val userPreferences: UserPreferences = koinInject()
    val isDarkMode by userPreferences.isDarkMode.collectAsState(initial = false)

    FinTrackTheme(darkTheme = isDarkMode) {
        // Inisialisasi NavController di sini
        val navController = rememberNavController()

        // Oper navController ke dalam AppNavHost
        AppNavHost(navController = navController)
    }
}