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

// ==================== ADD / EDIT SCREEN ====================

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditScreen(
    transactionId: Long?,
    onNavigateBack: () -> Unit
) {
    val isEditing = transactionId != null
    var selectedTab by remember { mutableStateOf(0) } // 0 = Expense, 1 = Income
    var amount by remember { mutableStateOf("") }
    var title by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("") }
    var date by remember { mutableStateOf("") }

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
                onTabSelected = { selectedTab = it }
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Amount display
            AmountDisplay(amount = amount)

            Spacer(modifier = Modifier.height(32.dp))

            // Form fields
            TransactionForm(
                title = title,
                onTitleChange = { title = it },
                category = category,
                date = date,
                amount = amount,
                onAmountChange = { amount = it }
            )

            Spacer(modifier = Modifier.weight(1f))

            // Save button
            Button(
                onClick = onNavigateBack,
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
        Row(
            verticalAlignment = Alignment.Bottom
        ) {
            Text(
                "$",
                style = MaterialTheme.typography.headlineMedium,
                color = TextGray
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                if (amount.isEmpty()) "0.00" else amount,
                style = MaterialTheme.typography.headlineLarge.copy(fontSize = 48.sp),
                color = if (amount.isEmpty()) TextMuted else TextWhite,
                fontWeight = FontWeight.Bold
            )
        }
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
    date: String,
    amount: String,
    onAmountChange: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
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
            value = if (category.isEmpty()) "" else category,
            onValueChange = {},
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
            readOnly = true,
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