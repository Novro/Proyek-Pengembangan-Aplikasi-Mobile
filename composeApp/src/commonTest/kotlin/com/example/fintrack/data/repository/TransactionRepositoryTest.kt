package com.example.fintrack.data.repository

import app.cash.turbine.test
import com.example.fintrack.domain.model.Transaction
import com.example.fintrack.domain.model.TransactionType
import com.example.fintrack.domain.repository.TransactionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.Clock
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class TransactionRepositoryTest {
    
    private lateinit var repository: FakeTransactionRepository
    
    @BeforeTest
    fun setup() {
        repository = FakeTransactionRepository()
    }
    
    @Test
    fun `insertTransaction should return new transaction id`() = runTest {
        val transaction = createTestTransaction(title = "Kopi")
        val id = repository.insertTransaction(transaction)
        assertTrue(id > 0)
    }
    
    @Test
    fun `insertTransaction should add transaction to list`() = runTest {
        val transaction = createTestTransaction(title = "Gaji")
        repository.insertTransaction(transaction)
        
        repository.getAllTransactions().test {
            val transactions = awaitItem()
            assertEquals(1, transactions.size)
            assertEquals("Gaji", transactions.first().title)
            cancelAndIgnoreRemainingEvents()
        }
    }
    
    @Test
    fun `getAllTransactions should return all transactions`() = runTest {
        repository.insertTransaction(createTestTransaction(title = "Tx 1"))
        repository.insertTransaction(createTestTransaction(title = "Tx 2"))
        
        repository.getAllTransactions().test {
            val transactions = awaitItem()
            assertEquals(2, transactions.size)
            cancelAndIgnoreRemainingEvents()
        }
    }
    
    @Test
    fun `getTransactionById should return correct transaction`() = runTest {
        val id = repository.insertTransaction(createTestTransaction(title = "Target"))
        
        repository.getTransactionById(id).test {
            val transaction = awaitItem()
            assertNotNull(transaction)
            assertEquals("Target", transaction.title)
            cancelAndIgnoreRemainingEvents()
        }
    }
    
    @Test
    fun `deleteTransaction should remove transaction`() = runTest {
        val id = repository.insertTransaction(createTestTransaction(title = "Delete Me"))
        repository.deleteTransaction(id)
        
        repository.getAllTransactions().test {
            val transactions = awaitItem()
            assertTrue(transactions.isEmpty())
            cancelAndIgnoreRemainingEvents()
        }
    }
    
    @Test
    fun `getTotalBalance should calculate correct balance`() = runTest {
        repository.insertTransaction(createTestTransaction(amount = 100.0, type = TransactionType.INCOME))
        repository.insertTransaction(createTestTransaction(amount = 40.0, type = TransactionType.EXPENSE))
        
        repository.getTotalBalance().test {
            val balance = awaitItem()
            assertEquals(60.0, balance)
            cancelAndIgnoreRemainingEvents()
        }
    }
    
    private fun createTestTransaction(
        id: Long = 0,
        title: String = "Test",
        amount: Double = 10.0,
        type: TransactionType = TransactionType.EXPENSE,
        category: String = "Food"
    ): Transaction {
        return Transaction(
            id = id,
            title = title,
            amount = amount,
            type = type,
            category = category,
            date = Clock.System.now(),
            createdAt = Clock.System.now(),
            updatedAt = Clock.System.now()
        )
    }
}

class FakeTransactionRepository : TransactionRepository {
    
    private val transactions = MutableStateFlow<List<Transaction>>(emptyList())
    private var nextId = 1L
    
    override fun getAllTransactions(): Flow<List<Transaction>> = transactions
    
    override fun getRecentTransactions(limit: Long): Flow<List<Transaction>> {
        return transactions.map { list -> list.take(limit.toInt()) }
    }
    
    override fun getTransactionById(id: Long): Flow<Transaction?> {
        return transactions.map { list -> list.find { it.id == id } }
    }
    
    override fun getTotalBalance(): Flow<Double> {
        return transactions.map { list ->
            list.sumOf { if (it.type == TransactionType.INCOME) it.amount else -it.amount }
        }
    }
    
    override suspend fun insertTransaction(transaction: Transaction): Long {
        val id = nextId++
        val newTx = transaction.copy(id = id)
        transactions.update { it + newTx }
        return id
    }
    
    override suspend fun updateTransaction(transaction: Transaction) {
        transactions.update { list ->
            list.map { if (it.id == transaction.id) transaction else it }
        }
    }
    
    override suspend fun deleteTransaction(id: Long) {
        transactions.update { list -> list.filter { it.id != id } }
    }
}
