package com.example.fintrack.presentation.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

// ==================== FINTRACK COLOR PALETTE ====================

// Primary brand colors
val FinTrackGreen = Color(0xFF1B9B7A)
val FinTrackGreenLight = Color(0xFF22C993)
val FinTrackGreenDark = Color(0xFF0E7A5E)

// Dark theme surfaces
val DarkBackground = Color(0xFF0D0F14)
val DarkSurface = Color(0xFF151921)
val DarkSurfaceVariant = Color(0xFF1C2230)
val DarkCard = Color(0xFF1A1F2B)
val DarkCardElevated = Color(0xFF212838)

// Text colors
val TextWhite = Color(0xFFF0F2F5)
val TextGray = Color(0xFF8E95A2)
val TextMuted = Color(0xFF5A6170)

// Semantic colors
val ErrorRed = Color(0xFFEF4444)
val ErrorRedBg = Color(0xFF2D1B1B)
val SuccessGreen = Color(0xFF22C55E)
val SuccessGreenBg = Color(0xFF132E1F)

// Gradient palette for balance card
val GradientStart = Color(0xFF0E3D2E)
val GradientMiddle = Color(0xFF135C44)
val GradientEnd = Color(0xFF1B8A63)

// ==================== COLOR SCHEME ====================

private val FinTrackDarkColorScheme = darkColorScheme(
    primary = FinTrackGreen,
    onPrimary = Color.White,
    primaryContainer = FinTrackGreenDark,
    onPrimaryContainer = FinTrackGreenLight,
    secondary = Color(0xFF3D8B74),
    onSecondary = Color.White,
    secondaryContainer = DarkCardElevated,
    onSecondaryContainer = TextGray,
    tertiary = Color(0xFF6C7BFF),
    error = ErrorRed,
    onError = Color.White,
    errorContainer = ErrorRedBg,
    background = DarkBackground,
    onBackground = TextWhite,
    surface = DarkSurface,
    onSurface = TextWhite,
    surfaceVariant = DarkSurfaceVariant,
    onSurfaceVariant = TextGray,
    outline = TextMuted,
    outlineVariant = Color(0xFF2A303C),
    inverseSurface = TextWhite,
    inverseOnSurface = DarkBackground,
    surfaceContainerLowest = Color(0xFF0A0C10),
    surfaceContainerLow = DarkSurface,
    surfaceContainer = DarkCard,
    surfaceContainerHigh = DarkCardElevated,
    surfaceContainerHighest = Color(0xFF282F3C)
)

// ==================== TYPOGRAPHY ====================

private val FinTrackTypography = Typography(
    headlineLarge = TextStyle(
        fontWeight = FontWeight.Bold,
        fontSize = 32.sp,
        lineHeight = 40.sp,
        letterSpacing = (-0.5).sp
    ),
    headlineMedium = TextStyle(
        fontWeight = FontWeight.Bold,
        fontSize = 24.sp,
        lineHeight = 32.sp
    ),
    headlineSmall = TextStyle(
        fontWeight = FontWeight.SemiBold,
        fontSize = 20.sp,
        lineHeight = 28.sp
    ),
    titleLarge = TextStyle(
        fontWeight = FontWeight.SemiBold,
        fontSize = 18.sp,
        lineHeight = 26.sp
    ),
    titleMedium = TextStyle(
        fontWeight = FontWeight.SemiBold,
        fontSize = 16.sp,
        lineHeight = 24.sp
    ),
    titleSmall = TextStyle(
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp,
        lineHeight = 20.sp
    ),
    bodyLarge = TextStyle(
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp
    ),
    bodyMedium = TextStyle(
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        lineHeight = 20.sp
    ),
    bodySmall = TextStyle(
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp,
        lineHeight = 16.sp
    ),
    labelLarge = TextStyle(
        fontWeight = FontWeight.SemiBold,
        fontSize = 14.sp,
        lineHeight = 20.sp
    ),
    labelMedium = TextStyle(
        fontWeight = FontWeight.Medium,
        fontSize = 12.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp
    ),
    labelSmall = TextStyle(
        fontWeight = FontWeight.Medium,
        fontSize = 10.sp,
        lineHeight = 14.sp,
        letterSpacing = 0.5.sp
    )
)

// ==================== THEME COMPOSABLE ====================

@Composable
fun FinTrackTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    // FinTrack is a dark-first design
    val colorScheme = FinTrackDarkColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = FinTrackTypography,
        content = content
    )
}