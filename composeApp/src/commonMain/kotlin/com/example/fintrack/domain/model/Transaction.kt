package com.example.fintrack.domain.model

import kotlinx.datetime.Clock
import kotlinx.datetime.Instant

data class Transaction(
    val id: Long = 0,
    val title: String,
    val amount: Double,
    val type: TransactionType,
    val category: String,
    val date: Instant = Clock.System.now(),
    val createdAt: Instant = Clock.System.now(),
    val updatedAt: Instant = Clock.System.now(),
    val currency: String = "USD"
)

enum class TransactionType {
    INCOME, EXPENSE;
    
    companion object {
        fun fromString(value: String): TransactionType {
            return entries.find { it.name == value } ?: EXPENSE
        }
    }
}
