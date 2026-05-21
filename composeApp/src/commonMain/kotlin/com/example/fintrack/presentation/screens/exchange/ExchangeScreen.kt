package com.example.fintrack.presentation.screens.exchange

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.SwapVert
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.fintrack.presentation.theme.*

// ==================== EXCHANGE SCREEN ====================

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExchangeScreen(
    onNavigateBack: () -> Unit
) {
    Scaffold(
        containerColor = DarkBackground,
        topBar = { ExchangeTopBar() }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ) {
            // Back + Title
            Row(
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    Icons.Default.ArrowBack,
                    contentDescription = "Back",
                    tint = TextGray,
                    modifier = Modifier
                        .size(20.dp)
                        .clickable { onNavigateBack() }
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    "Exchange Rates",
                    style = MaterialTheme.typography.headlineSmall,
                    color = TextWhite,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Converter card
            ConverterCard()

            Spacer(modifier = Modifier.height(24.dp))

            // Trending rates section
            TrendingRatesSection()
        }
    }
}

// ==================== TOP BAR ====================

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ExchangeTopBar() {
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

// ==================== CONVERTER CARD ====================

@Composable
private fun ConverterCard() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = DarkCard)
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Amount input
            Column(modifier = Modifier.fillMaxWidth()) {
                Text("Amount", style = MaterialTheme.typography.labelMedium, color = TextGray)
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = "$ 1.00",
                    onValueChange = {},
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = FinTrackGreen,
                        unfocusedBorderColor = DarkSurfaceVariant,
                        focusedContainerColor = DarkSurfaceVariant,
                        unfocusedContainerColor = DarkSurfaceVariant,
                        cursorColor = FinTrackGreen,
                        focusedTextColor = TextWhite,
                        unfocusedTextColor = TextWhite
                    ),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            // From currency
            CurrencySelector(code = "USD", name = "US Dollar")

            Spacer(modifier = Modifier.height(12.dp))

            // Swap icon
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(FinTrackGreen),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Default.SwapVert,
                    contentDescription = "Swap",
                    tint = Color.White,
                    modifier = Modifier.size(22.dp)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // To currency
            CurrencySelector(code = "IDR", name = "Indonesian Rupiah")

            Spacer(modifier = Modifier.height(24.dp))

            // Converted amount
            Text(
                "CONVERTED AMOUNT",
                style = MaterialTheme.typography.labelSmall,
                color = TextGray,
                letterSpacing = 1.5.sp
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                "Rp 15,700",
                style = MaterialTheme.typography.headlineLarge,
                color = FinTrackGreen,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                "Mid-market rate • Last updated 2 min ago",
                style = MaterialTheme.typography.labelSmall,
                color = TextMuted
            )
        }
    }
}

// ==================== CURRENCY SELECTOR ====================

@Composable
private fun CurrencySelector(code: String, name: String) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        color = DarkSurfaceVariant
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                // Currency badge
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(FinTrackGreen.copy(alpha = 0.15f)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        code.take(1),
                        color = FinTrackGreen,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(code, style = MaterialTheme.typography.labelSmall, color = TextGray)
                    Text(name, style = MaterialTheme.typography.bodyLarge, color = TextWhite, fontWeight = FontWeight.Medium)
                }
            }
            Icon(Icons.Default.KeyboardArrowDown, contentDescription = null, tint = TextGray)
        }
    }
}

// ==================== TRENDING RATES ====================

@Composable
private fun TrendingRatesSection() {
    Column(modifier = Modifier.padding(horizontal = 16.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                "Trending Rates",
                style = MaterialTheme.typography.titleMedium,
                color = TextWhite,
                fontWeight = FontWeight.Bold
            )
            Text(
                "See all",
                style = MaterialTheme.typography.labelMedium,
                color = FinTrackGreen,
                modifier = Modifier.clickable { }
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        val rates = listOf(
            ExchangeRate("Bitcoin", "BTC", "BTC/IDR", "Rp 1,050,000,000", "↗ 2.4%", true, Color(0xFFF7931A)),
            ExchangeRate("US Dollar", "USD", "USD/IDR", "Rp 15,700", "↗ 0.8%", false, Color(0xFF2196F3)),
            ExchangeRate("Ethereum", "ETH", "ETH/IDR", "Rp 50,000,000", "↘ 1.2%", true, Color(0xFF627EEA))
        )

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(bottom = 16.dp)
        ) {
            items(rates) { rate ->
                RateItem(rate)
            }
        }
    }
}

// ==================== RATE DATA ====================

private data class ExchangeRate(
    val name: String,
    val code: String,
    val pair: String,
    val rate: String,
    val change: String,
    val isCrypto: Boolean,
    val brandColor: Color
)

// ==================== RATE ITEM ====================

@Composable
private fun RateItem(rate: ExchangeRate) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = DarkCard)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Currency icon
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(rate.brandColor.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    rate.code.take(1),
                    color = rate.brandColor,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
            }

            Spacer(modifier = Modifier.width(14.dp))

            // Name & pair
            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        rate.name,
                        style = MaterialTheme.typography.bodyLarge,
                        color = TextWhite,
                        fontWeight = FontWeight.Medium
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    // Change badge
                    val isPositive = rate.change.contains("↗")
                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = if (isPositive) SuccessGreenBg else ErrorRedBg
                    ) {
                        Text(
                            rate.change,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                            style = MaterialTheme.typography.labelSmall,
                            color = if (isPositive) SuccessGreen else ErrorRed,
                            fontSize = 9.sp
                        )
                    }
                }
                Text(
                    rate.pair,
                    style = MaterialTheme.typography.labelSmall,
                    color = TextMuted
                )
            }

            // Rate value
            Text(
                rate.rate,
                style = MaterialTheme.typography.bodyLarge,
                color = TextWhite,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}