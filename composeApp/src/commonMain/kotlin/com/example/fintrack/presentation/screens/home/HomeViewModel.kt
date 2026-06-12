package com.example.fintrack.presentation.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fintrack.data.local.datastore.UserPreferences
import com.example.fintrack.data.remote.api.GeminiApiService
import com.example.fintrack.domain.model.Transaction
import com.example.fintrack.domain.repository.TransactionRepository
import com.example.fintrack.domain.model.TransactionType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class HomeViewModel(
    repository: TransactionRepository,
    userPreferences: UserPreferences? = null
) : ViewModel() {

    val recentTransactions: StateFlow<List<Transaction>> = repository.getRecentTransactions(5)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    private val allTransactions = repository.getAllTransactions()

    val lastIdrRate: StateFlow<Double> = (userPreferences?.lastIdrRate ?: flowOf(16000.0))
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = 16000.0
        )

    val totalBalance: StateFlow<Double> = allTransactions.map { transactions ->
        transactions.sumOf { tx ->
            if (tx.type == TransactionType.INCOME) tx.amount else -tx.amount
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = 0.0
    )

    val monthlyIncome: StateFlow<Double> = allTransactions.map { transactions ->
        transactions
            .filter { it.type == TransactionType.INCOME }
            .sumOf { it.amount }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = 0.0
    )

    val monthlyExpense: StateFlow<Double> = allTransactions.map { transactions ->
        transactions
            .filter { it.type == TransactionType.EXPENSE }
            .sumOf { it.amount }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = 0.0
    )

    val totalBalanceIdr: StateFlow<Double> = combine(
        totalBalance,
        lastIdrRate
    ) { balance, rate ->
        balance * rate
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = 0.0
    )

    val monthlyIncomeIdr: StateFlow<Double> = combine(
        monthlyIncome,
        lastIdrRate
    ) { income, rate ->
        income * rate
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = 0.0
    )

    val monthlyExpenseIdr: StateFlow<Double> = combine(
        monthlyExpense,
        lastIdrRate
    ) { expense, rate ->
        expense * rate
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = 0.0
    )

    private val geminiService = GeminiApiService()

    private val _aiInsightText = MutableStateFlow("Menganalisis pola pengeluaranmu...")
    val aiInsightText: StateFlow<String> = _aiInsightText.asStateFlow()

    private val _isAiLoading = MutableStateFlow(false)
    val isAiLoading: StateFlow<Boolean> = _isAiLoading.asStateFlow()

    private var hasFetchedInsight = false

    init {
        viewModelScope.launch {
            monthlyExpense.collect { expense ->
                if (expense > 0.0 && !hasFetchedInsight) {
                    hasFetchedInsight = true
                    fetchFinancialInsight(expense)
                }
            }
        }
    }

    fun fetchFinancialInsight(currentExpense: Double) {
        viewModelScope.launch {
            _isAiLoading.value = true
            val advice = geminiService.getFinancialAdvice(
                totalExpense = currentExpense,
                budget = 500.0,
                topCategory = "Makanan"
            )
            _aiInsightText.value = advice
            _isAiLoading.value = false
        }
    }
}
