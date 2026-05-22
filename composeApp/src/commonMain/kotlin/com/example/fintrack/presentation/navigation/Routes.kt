package com.example.fintrack.presentation.navigation

import kotlinx.serialization.Serializable

sealed class Screen {
    @Serializable
    object Home : Screen()

    @Serializable
    data class DetailTransaction(val transactionId: Long) : Screen()

    @Serializable
    object AddTransaction : Screen()

    @Serializable
    data class EditTransaction(val transactionId: Long) : Screen()

    @Serializable
    object Exchange : Screen()

    @Serializable
    object History : Screen()
}