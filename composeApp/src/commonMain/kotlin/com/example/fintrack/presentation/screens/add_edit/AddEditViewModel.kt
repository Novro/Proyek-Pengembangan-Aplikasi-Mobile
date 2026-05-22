package com.example.fintrack.presentation.screens.add_edit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fintrack.domain.model.Transaction
import com.example.fintrack.domain.model.TransactionType
import com.example.fintrack.domain.repository.TransactionRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock

class AddEditViewModel(
    private val repository: TransactionRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(AddEditState())
    val uiState = _uiState.asStateFlow()

    fun loadTransaction(id: Long) {
        viewModelScope.launch {
            repository.getTransactionById(id).collect { transaction ->
                if (transaction != null) {
                    _uiState.update {
                        it.copy(
                            id = transaction.id,
                            title = transaction.title,
                            amount = transaction.amount.toString(),
                            type = transaction.type,
                            category = transaction.category,
                            currency = transaction.currency
                        )
                    }
                }
            }
        }
    }

    fun onEvent(event: AddEditEvent) {
        when (event) {
            is AddEditEvent.TitleChanged -> _uiState.update { it.copy(title = event.title) }
            is AddEditEvent.AmountChanged -> _uiState.update { it.copy(amount = event.amount) }
            is AddEditEvent.TypeChanged -> _uiState.update { it.copy(type = event.type) }
            is AddEditEvent.CategoryChanged -> _uiState.update { it.copy(category = event.category) }
            is AddEditEvent.CurrencyChanged -> _uiState.update { it.copy(currency = event.currency) }
            is AddEditEvent.SaveTransaction -> saveTransaction(event.onSuccess)
        }
    }

    private fun saveTransaction(onSuccess: () -> Unit) {
        val state = _uiState.value
        val amountDouble = state.amount.toDoubleOrNull() ?: 0.0
        if (state.title.isBlank() || amountDouble <= 0.0) return

        val transaction = Transaction(
            id = state.id ?: 0,
            title = state.title,
            amount = amountDouble,
            type = state.type,
            category = state.category.ifBlank { "General" },
            date = Clock.System.now(),
            currency = state.currency
        )

        viewModelScope.launch {
            if (state.id == null || state.id == 0L) {
                repository.insertTransaction(transaction)
            } else {
                repository.updateTransaction(transaction)
            }
            onSuccess()
        }
    }
}

data class AddEditState(
    val id: Long? = null,
    val title: String = "",
    val amount: String = "",
    val type: TransactionType = TransactionType.EXPENSE,
    val category: String = "",
    val currency: String = "USD"
)

sealed class AddEditEvent {
    data class TitleChanged(val title: String) : AddEditEvent()
    data class AmountChanged(val amount: String) : AddEditEvent()
    data class TypeChanged(val type: TransactionType) : AddEditEvent()
    data class CategoryChanged(val category: String) : AddEditEvent()
    data class CurrencyChanged(val currency: String) : AddEditEvent()
    data class SaveTransaction(val onSuccess: () -> Unit) : AddEditEvent()
}
