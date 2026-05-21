package com.example.fintrack.presentation.screens.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.fintrack.presentation.theme.*

// ==================== DETAIL SCREEN ====================

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(
    transactionId: Long,
    onNavigateToEdit: (Long) -> Unit,
    onNavigateBack: () -> Unit
) {
    var showDeleteDialog by remember { mutableStateOf(false) }

    Scaffold(
        containerColor = DarkBackground,
        topBar = { DetailTopBar() },
        bottomBar = {
            DetailBottomActions(
                onDelete = { showDeleteDialog = true },
                onEdit = { onNavigateToEdit(transactionId) }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            // Back link
            Text(
                "← Back to Transactions",
                style = MaterialTheme.typography.bodySmall,
                color = TextGray,
                modifier = Modifier
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .clickable { onNavigateBack() }
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Transaction header card
            TransactionHeaderCard()

            Spacer(modifier = Modifier.height(24.dp))

            // Transaction details
            TransactionDetailsSection()
        }

        // Delete confirmation dialog
        if (showDeleteDialog) {
            AlertDialog(
                onDismissRequest = { showDeleteDialog = false },
                containerColor = DarkCard,
                titleContentColor = TextWhite,
                textContentColor = TextGray,
                confirmButton = {
                    TextButton(
                        onClick = {
                            showDeleteDialog = false
                            onNavigateBack()
                        }
                    ) {
                        Text("Confirm", color = ErrorRed)
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showDeleteDialog = false }) {
                        Text("Cancel", color = TextGray)
                    }
                },
                title = { Text("Delete Transaction") },
                text = { Text("Are you sure you want to delete this transaction? This action cannot be undone.") }
            )
        }
    }
}

// ==================== TOP BAR ====================

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DetailTopBar() {
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
                    Text("F", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                }
                Spacer(modifier = Modifier.width(10.dp))
                Text("FinTrack", style = MaterialTheme.typography.titleLarge, color = TextWhite)
            }
        },
        actions = {
            IconButton(onClick = { }) {
                Icon(Icons.Default.Notifications, contentDescription = "Notifications", tint = TextGray)
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(containerColor = DarkBackground)
    )
}

// ==================== TRANSACTION HEADER CARD ====================

@Composable
private fun TransactionHeaderCard() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = DarkCard)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Category icon
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(DarkSurfaceVariant),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Default.Restaurant,
                    contentDescription = "Food",
                    tint = FinTrackGreen,
                    modifier = Modifier.size(28.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                "Weekly Groceries",
                style = MaterialTheme.typography.titleLarge,
                color = TextWhite,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                "$250.00",
                style = MaterialTheme.typography.headlineLarge,
                color = TextWhite,
                fontWeight = FontWeight.Bold,
                fontSize = 36.sp
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Status badge
            Surface(
                shape = RoundedCornerShape(8.dp),
                color = SuccessGreenBg
            ) {
                Text(
                    "Cleared",
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp),
                    style = MaterialTheme.typography.labelMedium,
                    color = SuccessGreen
                )
            }
        }
    }
}

// ==================== TRANSACTION DETAILS ====================

@Composable
private fun TransactionDetailsSection() {
    Column(modifier = Modifier.padding(horizontal = 16.dp)) {
        // Date
        DetailRow(label = "Date", value = "Oct 24, 2023")

        HorizontalDivider(
            modifier = Modifier.padding(vertical = 16.dp),
            color = DarkSurfaceVariant,
            thickness = 1.dp
        )

        // Category
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Category", style = MaterialTheme.typography.bodyMedium, color = TextGray)
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(10.dp)
                        .clip(CircleShape)
                        .background(FinTrackGreen)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("Food", style = MaterialTheme.typography.bodyLarge, color = TextWhite, fontWeight = FontWeight.Medium)
            }
        }

        HorizontalDivider(
            modifier = Modifier.padding(vertical = 16.dp),
            color = DarkSurfaceVariant,
            thickness = 1.dp
        )

        // Note
        Text("Note", style = MaterialTheme.typography.bodyMedium, color = TextGray)
        Spacer(modifier = Modifier.height(12.dp))
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = DarkCard)
        ) {
            Text(
                "Stocked up on essentials for the week. Included some specialty items for the dinner party on Saturday.",
                modifier = Modifier.padding(16.dp),
                style = MaterialTheme.typography.bodyMedium,
                color = TextGray,
                lineHeight = 22.sp
            )
        }
    }
}

@Composable
private fun DetailRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(label, style = MaterialTheme.typography.bodyMedium, color = TextGray)
        Text(value, style = MaterialTheme.typography.bodyLarge, color = TextWhite, fontWeight = FontWeight.Medium)
    }
}

// ==================== BOTTOM ACTIONS ====================

@Composable
private fun DetailBottomActions(
    onDelete: () -> Unit,
    onEdit: () -> Unit
) {
    Surface(
        color = DarkBackground,
        tonalElevation = 0.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Delete button
            OutlinedButton(
                onClick = onDelete,
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = ErrorRed),
                border = ButtonDefaults.outlinedButtonBorder(enabled = true).copy(
                    brush = androidx.compose.ui.graphics.Brush.linearGradient(
                        listOf(ErrorRed.copy(alpha = 0.5f), ErrorRed.copy(alpha = 0.5f))
                    )
                ),
                contentPadding = PaddingValues(vertical = 14.dp)
            ) {
                Icon(Icons.Default.Delete, contentDescription = null, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text("Delete", style = MaterialTheme.typography.labelLarge)
            }

            // Edit button
            Button(
                onClick = onEdit,
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = FinTrackGreen,
                    contentColor = Color.White
                ),
                contentPadding = PaddingValues(vertical = 14.dp)
            ) {
                Icon(Icons.Default.Edit, contentDescription = null, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text("Edit", style = MaterialTheme.typography.labelLarge)
            }
        }
    }
}