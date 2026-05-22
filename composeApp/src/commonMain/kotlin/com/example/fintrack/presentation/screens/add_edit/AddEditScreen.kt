package com.example.fintrack.presentation.screens.add_edit

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.fintrack.presentation.theme.*
import com.example.fintrack.domain.model.TransactionType
import org.koin.compose.viewmodel.koinViewModel

// ==================== ADD / EDIT SCREEN ====================

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditScreen(
    transactionId: Long?,
    onNavigateBack: () -> Unit,
    viewModel: AddEditViewModel = koinViewModel()
) {
    val isEditing = transactionId != null
    val state by viewModel.uiState.collectAsState()
    
    // Init data when editing
    LaunchedEffect(transactionId) {
        if (transactionId != null) {
            viewModel.loadTransaction(transactionId)
        }
    }

    val selectedTab = if (state.type == TransactionType.EXPENSE) 0 else 1

    Scaffold(
        containerColor = DarkBackground,
        topBar = {
            AddEditTopBar(
                isEditing = isEditing,
                onClose = onNavigateBack
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            Spacer(modifier = Modifier.height(8.dp))

            // Expense / Income toggle
            TransactionTypeToggle(
                selectedTab = selectedTab,
                onTabSelected = { 
                    viewModel.onEvent(AddEditEvent.TypeChanged(if (it == 0) TransactionType.EXPENSE else TransactionType.INCOME))
                }
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Amount display
            AmountDisplay(amount = state.amount)

            Spacer(modifier = Modifier.height(32.dp))

            // Form fields
            TransactionForm(
                title = state.title,
                onTitleChange = { viewModel.onEvent(AddEditEvent.TitleChanged(it)) },
                category = state.category,
                onCategoryChange = { viewModel.onEvent(AddEditEvent.CategoryChanged(it)) },
                date = "Now", // Simplified for now
                amount = state.amount,
                onAmountChange = { viewModel.onEvent(AddEditEvent.AmountChanged(it)) },
                currency = state.currency,
                onCurrencyChange = { viewModel.onEvent(AddEditEvent.CurrencyChanged(it)) }
            )

            Spacer(modifier = Modifier.weight(1f))

            // Save button
            Button(
                onClick = {
                    viewModel.onEvent(AddEditEvent.SaveTransaction(onSuccess = onNavigateBack))
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 16.dp),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = FinTrackGreen,
                    contentColor = Color.White
                ),
                contentPadding = PaddingValues(vertical = 16.dp)
            ) {
                Icon(
                    Icons.Default.Check,
                    contentDescription = null,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    "Save Transaction",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}

// ==================== TOP BAR ====================

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AddEditTopBar(isEditing: Boolean, onClose: () -> Unit) {
    TopAppBar(
        title = {
            Text(
                if (isEditing) "Edit Transaction" else "New Transaction",
                style = MaterialTheme.typography.titleMedium,
                color = TextWhite
            )
        },
        navigationIcon = {
            IconButton(onClick = onClose) {
                Icon(Icons.Default.Close, contentDescription = "Close", tint = TextGray)
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(containerColor = DarkBackground)
    )
}

// ==================== TYPE TOGGLE ====================

@Composable
private fun TransactionTypeToggle(selectedTab: Int, onTabSelected: (Int) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 48.dp),
        horizontalArrangement = Arrangement.Center
    ) {
        val tabs = listOf("Expense", "Income")
        tabs.forEachIndexed { index, label ->
            val isSelected = selectedTab == index
            Surface(
                modifier = Modifier
                    .weight(1f)
                    .clickable { onTabSelected(index) },
                shape = RoundedCornerShape(12.dp),
                color = if (isSelected) DarkCard else Color.Transparent,
                border = if (isSelected) null else ButtonDefaults.outlinedButtonBorder(enabled = true).copy(
                    brush = androidx.compose.ui.graphics.Brush.linearGradient(
                        listOf(DarkSurfaceVariant, DarkSurfaceVariant)
                    )
                )
            ) {
                Text(
                    label,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
                    style = MaterialTheme.typography.labelLarge,
                    color = if (isSelected) TextWhite else TextGray,
                    textAlign = TextAlign.Center
                )
            }
            if (index == 0) Spacer(modifier = Modifier.width(8.dp))
        }
    }
}

// ==================== AMOUNT DISPLAY ====================

@Composable
private fun AmountDisplay(amount: String) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Quick amount editor
        OutlinedTextField(
            value = amount,
            onValueChange = { /* We edit in form below instead */ },
            modifier = Modifier.fillMaxWidth().padding(horizontal = 32.dp),
            textStyle = MaterialTheme.typography.headlineLarge.copy(fontSize = 32.sp, textAlign = TextAlign.Center),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color.Transparent,
                unfocusedBorderColor = Color.Transparent,
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                cursorColor = FinTrackGreen,
                focusedTextColor = TextWhite,
                unfocusedTextColor = TextMuted
            ),
            placeholder = { Text("0.00", style = MaterialTheme.typography.headlineLarge.copy(fontSize = 32.sp), color = TextMuted, textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth()) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            readOnly = true
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            "Tap to edit amount",
            style = MaterialTheme.typography.bodySmall,
            color = TextMuted
        )
    }
}

// ==================== FORM FIELDS ====================

@Composable
private fun TransactionForm(
    title: String,
    onTitleChange: (String) -> Unit,
    category: String,
    onCategoryChange: (String) -> Unit,
    date: String,
    amount: String,
    onAmountChange: (String) -> Unit,
    currency: String,
    onCurrencyChange: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Currency Selector
        Text("Currency", style = MaterialTheme.typography.labelMedium, color = TextGray)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            val currencies = listOf("USD" to "Dollar ($)", "IDR" to "Rupiah (Rp)")
            currencies.forEach { (code, label) ->
                val isSelected = currency == code
                Surface(
                    modifier = Modifier
                        .weight(1f)
                        .clickable { onCurrencyChange(code) },
                    shape = RoundedCornerShape(12.dp),
                    color = if (isSelected) FinTrackGreen.copy(alpha = 0.15f) else DarkCard,
                    border = ButtonDefaults.outlinedButtonBorder(enabled = true).copy(
                        brush = androidx.compose.ui.graphics.Brush.linearGradient(
                            if (isSelected) listOf(FinTrackGreen, FinTrackGreen)
                            else listOf(DarkSurfaceVariant, DarkSurfaceVariant)
                        )
                    )
                ) {
                    Text(
                        label,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
                        style = MaterialTheme.typography.bodyMedium,
                        color = if (isSelected) FinTrackGreen else TextGray,
                        textAlign = TextAlign.Center,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                    )
                }
            }
        }

        // Amount field (Moved here for easier input)
        Text("Amount", style = MaterialTheme.typography.labelMedium, color = TextGray)
        OutlinedTextField(
            value = amount,
            onValueChange = onAmountChange,
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("0.00", color = TextMuted) },
            leadingIcon = { Text(if (currency == "USD") "$" else "Rp", color = TextGray, modifier = Modifier.padding(start = 16.dp, end = 8.dp)) },
            shape = RoundedCornerShape(14.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = FinTrackGreen,
                unfocusedBorderColor = DarkSurfaceVariant,
                focusedContainerColor = DarkCard,
                unfocusedContainerColor = DarkCard,
                cursorColor = FinTrackGreen,
                focusedTextColor = TextWhite,
                unfocusedTextColor = TextWhite
            ),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            singleLine = true
        )

        // Title field
        Text("Title", style = MaterialTheme.typography.labelMedium, color = TextGray)
        OutlinedTextField(
            value = title,
            onValueChange = onTitleChange,
            modifier = Modifier.fillMaxWidth(),
            placeholder = {
                Text("e.g. Groceries at Trader Joe's", color = TextMuted)
            },
            leadingIcon = {
                Icon(Icons.Default.Description, contentDescription = null, tint = TextGray, modifier = Modifier.size(20.dp))
            },
            shape = RoundedCornerShape(14.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = FinTrackGreen,
                unfocusedBorderColor = DarkSurfaceVariant,
                focusedContainerColor = DarkCard,
                unfocusedContainerColor = DarkCard,
                cursorColor = FinTrackGreen,
                focusedTextColor = TextWhite,
                unfocusedTextColor = TextWhite
            ),
            singleLine = true
        )

        // Category field
        Text("Category", style = MaterialTheme.typography.labelMedium, color = TextGray)
        OutlinedTextField(
            value = category,
            onValueChange = onCategoryChange,
            modifier = Modifier.fillMaxWidth(),
            placeholder = {
                Text("Select a category", color = TextMuted)
            },
            leadingIcon = {
                Icon(Icons.Default.Category, contentDescription = null, tint = TextGray, modifier = Modifier.size(20.dp))
            },
            trailingIcon = {
                Icon(Icons.Default.KeyboardArrowDown, contentDescription = null, tint = TextGray)
            },
            shape = RoundedCornerShape(14.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = FinTrackGreen,
                unfocusedBorderColor = DarkSurfaceVariant,
                focusedContainerColor = DarkCard,
                unfocusedContainerColor = DarkCard,
                cursorColor = FinTrackGreen,
                focusedTextColor = TextWhite,
                unfocusedTextColor = TextWhite
            ),
            singleLine = true
        )

        // Date field
        Text("Date", style = MaterialTheme.typography.labelMedium, color = TextGray)
        OutlinedTextField(
            value = if (date.isEmpty()) "10/27/2023" else date,
            onValueChange = {},
            modifier = Modifier.fillMaxWidth(),
            leadingIcon = {
                Icon(Icons.Default.CalendarToday, contentDescription = null, tint = TextGray, modifier = Modifier.size(20.dp))
            },
            trailingIcon = {
                Icon(Icons.Default.CalendarToday, contentDescription = null, tint = TextGray, modifier = Modifier.size(18.dp))
            },
            shape = RoundedCornerShape(14.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = FinTrackGreen,
                unfocusedBorderColor = DarkSurfaceVariant,
                focusedContainerColor = DarkCard,
                unfocusedContainerColor = DarkCard,
                cursorColor = FinTrackGreen,
                focusedTextColor = TextWhite,
                unfocusedTextColor = TextWhite
            ),
            readOnly = true,
            singleLine = true
        )

        // Add Note or Receipt
        Text(
            "+ Add Note or Receipt",
            style = MaterialTheme.typography.bodyMedium,
            color = FinTrackGreen,
            modifier = Modifier
                .clickable { }
                .padding(vertical = 8.dp)
        )
    }
}