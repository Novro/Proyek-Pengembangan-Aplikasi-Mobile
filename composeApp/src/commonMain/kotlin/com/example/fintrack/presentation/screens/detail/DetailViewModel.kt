package com.example.fintrack.presentation.screens.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fintrack.domain.model.Transaction
import com.example.fintrack.domain.repository.TransactionRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class DetailViewModel(
    private val repository: TransactionRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<DetailState>(DetailState.Loading)
    val uiState = _uiState.asStateFlow()

    fun loadTransaction(id: Long) {
        viewModelScope.launch {
            repository.getTransactionById(id).collect { transaction ->
                if (transaction != null) {
                    _uiState.update { DetailState.Success(transaction) }
                } else {
                    _uiState.update { DetailState.Error("Transaction not found") }
                }
            }
        }
    }

    fun deleteTransaction(id: Long, onSuccess: () -> Unit) {
        viewModelScope.launch {
            repository.deleteTransaction(id)
            onSuccess()
        }
    }
}

sealed class DetailState {
    object Loading : DetailState()
    data class Success(val transaction: Transaction) : DetailState()
    data class Error(val message: String) : DetailState()
}
