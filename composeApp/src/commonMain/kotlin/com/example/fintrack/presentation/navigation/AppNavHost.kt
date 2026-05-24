package com.example.fintrack.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.example.fintrack.presentation.screens.home.HomeScreen
import com.example.fintrack.presentation.screens.detail.DetailScreen
import com.example.fintrack.presentation.screens.add_edit.AddEditScreen
import com.example.fintrack.presentation.screens.exchange.ExchangeScreen
import com.example.fintrack.presentation.screens.history.HistoryScreen
import com.example.fintrack.presentation.screens.settings.SettingsScreen

@Composable
fun AppNavHost(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Screen.Home
    ) {
        composable<Screen.Home> {
            HomeScreen(
                onNavigateToDetail = { id ->
                    navController.navigate(Screen.DetailTransaction(id))
                },
                onNavigateToAdd = {
                    navController.navigate(Screen.AddTransaction)
                },
                onNavigateToExchange = {
                    navController.navigate(Screen.Exchange)
                },
                onNavigateToHistory = {
                    navController.navigate(Screen.History)
                },
                onNavigateToSettings = {
                    navController.navigate(Screen.Settings)
                }
            )
        }

        composable<Screen.DetailTransaction> { backStackEntry ->
            val route: Screen.DetailTransaction = backStackEntry.toRoute()
            DetailScreen(
                transactionId = route.transactionId,
                onNavigateToEdit = { id ->
                    navController.navigate(Screen.EditTransaction(id))
                },
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable<Screen.AddTransaction> {
            AddEditScreen(
                transactionId = null,
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable<Screen.EditTransaction> { backStackEntry ->
            val route: Screen.EditTransaction = backStackEntry.toRoute()
            AddEditScreen(
                transactionId = route.transactionId,
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable<Screen.Exchange> {
            ExchangeScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable<Screen.History> {
            HistoryScreen(
                onNavigateToDetail = { id ->
                    navController.navigate(Screen.DetailTransaction(id))
                },
                onNavigateToAdd = {
                    navController.navigate(Screen.AddTransaction)
                },
                onNavigateToDashboard = {
                    navController.navigate(Screen.Home) {
                        popUpTo(Screen.Home) { inclusive = true }
                    }
                },
                onNavigateToExchange = {
                    navController.navigate(Screen.Exchange)
                }
            )
        }

        composable<Screen.Settings> {
            SettingsScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }
    }
}