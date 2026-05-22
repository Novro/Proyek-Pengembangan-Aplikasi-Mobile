package com.example.fintrack.presentation.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CurrencyExchange
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material.icons.outlined.AutoAwesome
import androidx.compose.material.icons.outlined.Description
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.fintrack.presentation.theme.*
import com.example.fintrack.domain.model.Transaction
import com.example.fintrack.domain.model.TransactionType
import org.koin.compose.viewmodel.koinViewModel
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import kotlin.math.roundToLong

// Helper for formatting currencies
private fun formatAmount(amount: Double, currency: String): String {
    val isNegative = amount < 0
    val absAmount = if (isNegative) -amount else amount
    val formatted = if (currency == "USD") {
        val rounded = (absAmount * 100).roundToLong() / 100.0
        val str = rounded.toString()
        val parts = str.split(".")
        val whole = parts[0]
        val decimal = if (parts.size > 1) parts[1].padEnd(2, '0').take(2) else "00"
        "$$whole.$decimal"
    } else {
        val rounded = absAmount.roundToLong()
        "Rp $rounded"
    }
    return if (isNegative) "-$formatted" else formatted
}

// ==================== HOME SCREEN ====================

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onNavigateToDetail: (Long) -> Unit,
    onNavigateToAdd: () -> Unit,
    onNavigateToExchange: () -> Unit = {},
    onNavigateToHistory: () -> Unit = {},
    viewModel: HomeViewModel = koinViewModel()
) {
    val recentTransactions by viewModel.recentTransactions.collectAsState()
    val totalBalance by viewModel.totalBalance.collectAsState()
    val displayCurrency by viewModel.displayCurrency.collectAsState()

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = { FinTrackTopBar() },
        bottomBar = {
            FinTrackBottomBar(
                onNavigateToExchange = onNavigateToExchange,
                onNavigateToHistory = onNavigateToHistory
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onNavigateToAdd,
                containerColor = FinTrackGreen,
                contentColor = Color.White,
                shape = CircleShape
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add Transaction")
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            BalanceCard(
                balance = totalBalance,
                currency = displayCurrency,
                onToggleCurrency = { viewModel.toggleDisplayCurrency() }
            )
            Spacer(modifier = Modifier.height(16.dp))
            MonthlyOverviewRow()
            Spacer(modifier = Modifier.height(24.dp))
            RecentTransactionsSection(
                transactions = recentTransactions,
                onNavigateToDetail = onNavigateToDetail,
                onNavigateToAdd = onNavigateToAdd,
                onViewAllClick = onNavigateToHistory
            )
        }
    }
}

// ==================== TOP BAR ====================

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun FinTrackTopBar() {
    TopAppBar(
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(CircleShape)
                        .background(FinTrackGreen),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        "F",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                }
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    "FinTrack",
                    style = MaterialTheme.typography.titleLarge,
                    color = TextWhite
                )
            }
        },
        actions = {
            IconButton(onClick = { }) {
                Icon(
                    Icons.Default.Notifications,
                    contentDescription = "Notifications",
                    tint = TextGray
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = DarkBackground
        )
    )
}

// ==================== BALANCE CARD ====================

@Composable
private fun BalanceCard(
    balance: Double = 0.0,
    currency: String = "USD",
    onToggleCurrency: () -> Unit = {}
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    brush = Brush.linearGradient(
                        colors = listOf(GradientStart, GradientMiddle, GradientEnd)
                    ),
                    shape = RoundedCornerShape(20.dp)
                )
                .padding(24.dp)
        ) {
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        "TOTAL BALANCE",
                        style = MaterialTheme.typography.labelMedium,
                        color = Color.White.copy(alpha = 0.7f),
                        letterSpacing = 1.5.sp
                    )
                    // Toggle currency pill
                    Surface(
                        modifier = Modifier.clickable { onToggleCurrency() },
                        shape = RoundedCornerShape(8.dp),
                        color = Color.White.copy(alpha = 0.2f)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                        ) {
                            Text(
                                text = if (currency == "USD") "USD ⇆ IDR" else "IDR ⇆ USD",
                                style = MaterialTheme.typography.labelSmall,
                                color = Color.White,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    val formatted = remember(balance, currency) {
                        formatAmount(balance, currency)
                    }
                    Text(
                        formatted,
                        style = MaterialTheme.typography.headlineLarge,
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                }
                Spacer(modifier = Modifier.height(20.dp))

                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    Button(
                        onClick = { },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = FinTrackGreen,
                            contentColor = Color.White
                        ),
                        shape = RoundedCornerShape(12.dp),
                        contentPadding = PaddingValues(horizontal = 20.dp, vertical = 10.dp)
                    ) {
                        Text("▶  Send", style = MaterialTheme.typography.labelLarge)
                    }
                    OutlinedButton(
                        onClick = { },
                        border = ButtonDefaults.outlinedButtonBorder(enabled = true).copy(
                            brush = Brush.linearGradient(listOf(Color.White.copy(alpha = 0.4f), Color.White.copy(alpha = 0.4f)))
                        ),
                        shape = RoundedCornerShape(12.dp),
                        contentPadding = PaddingValues(horizontal = 20.dp, vertical = 10.dp)
                    ) {
                        Text(
                            "⊹  Request",
                            style = MaterialTheme.typography.labelLarge,
                            color = Color.White
                        )
                    }
                }
            }
        }
    }
}

// ==================== MONTHLY OVERVIEW ====================

@Composable
private fun MonthlyOverviewRow() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        MonthlyCard(
            modifier = Modifier.weight(1f),
            label = "Monthly Income",
            amount = "$3,400.00",
            iconColor = FinTrackGreen,
            isIncome = true
        )
        MonthlyCard(
            modifier = Modifier.weight(1f),
            label = "Monthly Expenses",
            amount = "$2,150.00",
            iconColor = ErrorRed,
            isIncome = false
        )
    }
}

@Composable
private fun MonthlyCard(
    modifier: Modifier = Modifier,
    label: String,
    amount: String,
    iconColor: Color,
    isIncome: Boolean
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = DarkCard)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(28.dp)
                        .clip(CircleShape)
                        .background(iconColor.copy(alpha = 0.15f)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        if (isIncome) "↓" else "↑",
                        color = iconColor,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                }
            }
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                label,
                style = MaterialTheme.typography.labelSmall,
                color = TextGray
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                amount,
                style = MaterialTheme.typography.titleMedium,
                color = TextWhite,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

// ==================== RECENT TRANSACTIONS ====================

@Composable
private fun RecentTransactionsSection(
    transactions: List<Transaction>,
    onNavigateToDetail: (Long) -> Unit,
    onNavigateToAdd: () -> Unit,
    onViewAllClick: () -> Unit = {}
) {
    Column(modifier = Modifier.padding(horizontal = 16.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                "Recent Transactions",
                style = MaterialTheme.typography.titleMedium,
                color = TextWhite,
                fontWeight = FontWeight.Bold
            )
            Text(
                "View All >",
                style = MaterialTheme.typography.labelMedium,
                color = FinTrackGreen,
                modifier = Modifier.clickable { onViewAllClick() }
            )
        }
        Spacer(modifier = Modifier.height(16.dp))

        if (transactions.isEmpty()) {
            // Empty state
            EmptyTransactionsState(onAddClick = onNavigateToAdd)
        } else {
            transactions.forEach { transaction ->
                TransactionItem(transaction = transaction, onClick = { onNavigateToDetail(transaction.id) })
                Spacer(modifier = Modifier.height(12.dp))
            }
        }
    }
}

@Composable
private fun TransactionItem(transaction: Transaction, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(DarkCard)
            .clickable(onClick = onClick)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
                .background(DarkSurfaceVariant),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                if (transaction.type == TransactionType.INCOME) Icons.Default.TrendingUp else Icons.Default.Receipt,
                contentDescription = null,
                tint = if (transaction.type == TransactionType.INCOME) FinTrackGreen else Color.White
            )
        }
        Spacer(modifier = Modifier.width(16.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                transaction.title,
                style = MaterialTheme.typography.titleMedium,
                color = TextWhite,
                fontWeight = FontWeight.SemiBold
            )
            Text(
                transaction.category,
                style = MaterialTheme.typography.bodySmall,
                color = TextGray
            )
        }
        val prefix = if (transaction.type == TransactionType.INCOME) "+" else "-"
        val formattedAmount = remember(transaction.amount, transaction.currency) {
            formatAmount(transaction.amount, transaction.currency)
        }
        Text(
            "$prefix$formattedAmount",
            style = MaterialTheme.typography.titleMedium,
            color = if (transaction.type == TransactionType.INCOME) FinTrackGreen else TextWhite,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
private fun EmptyTransactionsState(onAddClick: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = DarkCard)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Icon
            Box(
                modifier = Modifier
                    .size(72.dp)
                    .clip(CircleShape)
                    .background(DarkSurfaceVariant),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Outlined.Description,
                    contentDescription = null,
                    tint = TextGray,
                    modifier = Modifier.size(32.dp)
                )
            }

            // Sparkle badge
            Box(
                modifier = Modifier
                    .offset(x = 20.dp, y = (-16).dp)
                    .size(20.dp)
                    .clip(CircleShape)
                    .background(FinTrackGreen),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Outlined.AutoAwesome,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(12.dp)
                )
            }

            Text(
                "No Recent Activity",
                style = MaterialTheme.typography.titleMedium,
                color = TextWhite,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                "It looks like you haven't made any\ntransactions recently. Track your\nspending by adding your first\ntransaction now.",
                style = MaterialTheme.typography.bodySmall,
                color = TextGray,
                lineHeight = 18.sp,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
            Spacer(modifier = Modifier.height(20.dp))
            OutlinedButton(
                onClick = onAddClick,
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = TextWhite)
            ) {
                Text("Add First Transaction", style = MaterialTheme.typography.labelLarge)
            }
        }
    }
}

// ==================== BOTTOM NAVIGATION ====================

@Composable
private fun FinTrackBottomBar(
    onNavigateToExchange: () -> Unit,
    onNavigateToHistory: () -> Unit
) {
    NavigationBar(
        containerColor = DarkSurface,
        contentColor = TextGray,
        tonalElevation = 0.dp
    ) {
        NavigationBarItem(
            selected = true,
            onClick = { },
            icon = { Icon(Icons.Default.Dashboard, contentDescription = "Dashboard") },
            label = { Text("Dashboard", style = MaterialTheme.typography.labelSmall) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = FinTrackGreen,
                selectedTextColor = FinTrackGreen,
                unselectedIconColor = TextMuted,
                unselectedTextColor = TextMuted,
                indicatorColor = FinTrackGreen.copy(alpha = 0.1f)
            )
        )
        NavigationBarItem(
            selected = false,
            onClick = onNavigateToExchange,
            icon = { Icon(Icons.Default.CurrencyExchange, contentDescription = "Exchange") },
            label = { Text("Exchange", style = MaterialTheme.typography.labelSmall) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = FinTrackGreen,
                selectedTextColor = FinTrackGreen,
                unselectedIconColor = TextMuted,
                unselectedTextColor = TextMuted,
                indicatorColor = FinTrackGreen.copy(alpha = 0.1f)
            )
        )
        NavigationBarItem(
            selected = false,
            onClick = onNavigateToHistory,
            icon = { Icon(Icons.Default.Receipt, contentDescription = "Transactions") },
            label = { Text("Transactions", style = MaterialTheme.typography.labelSmall) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = FinTrackGreen,
                selectedTextColor = FinTrackGreen,
                unselectedIconColor = TextMuted,
                unselectedTextColor = TextMuted,
                indicatorColor = FinTrackGreen.copy(alpha = 0.1f)
            )
        )
    }
}