package com.example.fintrack.domain.repository

import com.example.fintrack.domain.model.Transaction
import kotlinx.coroutines.flow.Flow

interface TransactionRepository {
    fun getAllTransactions(): Flow<List<Transaction>>
    fun getRecentTransactions(limit: Long): Flow<List<Transaction>>
    fun getTransactionById(id: Long): Flow<Transaction?>
    fun getTotalBalance(): Flow<Double>
    suspend fun insertTransaction(transaction: Transaction): Long
    suspend fun updateTransaction(transaction: Transaction)
    suspend fun deleteTransaction(id: Long)
}
