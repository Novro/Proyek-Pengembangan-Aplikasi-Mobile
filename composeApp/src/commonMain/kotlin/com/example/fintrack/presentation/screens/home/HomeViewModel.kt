package com.example.fintrack.presentation.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fintrack.domain.model.Transaction
import com.example.fintrack.domain.repository.TransactionRepository
import com.example.fintrack.domain.model.TransactionType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update

class HomeViewModel(
    repository: TransactionRepository
) : ViewModel() {

    val recentTransactions: StateFlow<List<Transaction>> = repository.getRecentTransactions(5)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    private val _displayCurrency = MutableStateFlow("USD")
    val displayCurrency = _displayCurrency.asStateFlow()

    val totalBalance: StateFlow<Double> = combine(
        repository.getAllTransactions(),
        _displayCurrency
    ) { transactions, currency ->
        transactions.sumOf { tx ->
            val amountInTargetCurrency = if (currency == "USD") {
                if (tx.currency == "USD") tx.amount else tx.amount / 16000.0
            } else {
                if (tx.currency == "IDR") tx.amount else tx.amount * 16000.0
            }
            if (tx.type == TransactionType.INCOME) amountInTargetCurrency else -amountInTargetCurrency
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = 0.0
    )

    fun toggleDisplayCurrency() {
        _displayCurrency.update { if (it == "USD") "IDR" else "USD" }
    }
}
