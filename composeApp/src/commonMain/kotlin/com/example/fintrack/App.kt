package com.example.fintrack

import androidx.compose.runtime.Composable
import androidx.navigation.compose.rememberNavController
import com.example.fintrack.presentation.theme.FinTrackTheme
import com.example.fintrack.presentation.navigation.AppNavHost

@Composable
fun App() {
    FinTrackTheme {
        // Inisialisasi NavController di sini
        val navController = rememberNavController()

        // Oper navController ke dalam AppNavHost
        AppNavHost(navController = navController)
    }
}