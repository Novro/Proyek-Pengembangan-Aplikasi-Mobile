package com.example.fintrack.presentation.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
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

// Raw Dark Theme Colors
private val RawDarkBackground = Color(0xFF0D0F14)
private val RawDarkSurface = Color(0xFF151921)
private val RawDarkSurfaceVariant = Color(0xFF1C2230)
private val RawDarkCard = Color(0xFF1A1F2B)
private val RawDarkCardElevated = Color(0xFF212838)

// Raw Dark Theme Text colors
private val RawTextWhite = Color(0xFFF0F2F5)
private val RawTextGray = Color(0xFF8E95A2)
private val RawTextMuted = Color(0xFF5A6170)

// Raw Light Theme Colors
private val RawLightBackground = Color(0xFFF9FAFB)
private val RawLightSurface = Color(0xFFFFFFFF)
private val RawLightSurfaceVariant = Color(0xFFE5E7EB)
private val RawLightCard = Color(0xFFFFFFFF)
private val RawLightCardElevated = Color(0xFFF3F4F6)

// Raw Light Theme Text colors
private val RawTextDark = Color(0xFF111827)
private val RawTextGrayDark = Color(0xFF4B5563)
private val RawTextMutedDark = Color(0xFF9CA3AF)

// Public Color Names as Dynamic Composable Getters
val DarkBackground: Color @Composable get() = MaterialTheme.colorScheme.background
val DarkSurface: Color @Composable get() = MaterialTheme.colorScheme.surface
val DarkSurfaceVariant: Color @Composable get() = MaterialTheme.colorScheme.surfaceVariant
val DarkCard: Color @Composable get() = MaterialTheme.colorScheme.surfaceContainer
val DarkCardElevated: Color @Composable get() = MaterialTheme.colorScheme.surfaceContainerHigh
val TextWhite: Color @Composable get() = MaterialTheme.colorScheme.onBackground
val TextGray: Color @Composable get() = MaterialTheme.colorScheme.onSurfaceVariant
val TextMuted: Color @Composable get() = MaterialTheme.colorScheme.outline

// Semantic colors
val ErrorRed = Color(0xFFEF4444)
val ErrorRedBg = Color(0xFF2D1B1B)
val SuccessGreen = Color(0xFF22C55E)
val SuccessGreenBg = Color(0xFF132E1F)

// Gradient palette for balance card
val GradientStart = Color(0xFF0E3D2E)
val GradientMiddle = Color(0xFF135C44)
val GradientEnd = Color(0xFF1B8A63)

// ==================== COLOR SCHEMES ====================

private val FinTrackDarkColorScheme = darkColorScheme(
    primary = FinTrackGreen,
    onPrimary = Color.White,
    primaryContainer = FinTrackGreenDark,
    onPrimaryContainer = FinTrackGreenLight,
    secondary = Color(0xFF3D8B74),
    onSecondary = Color.White,
    secondaryContainer = RawDarkCardElevated,
    onSecondaryContainer = RawTextGray,
    tertiary = Color(0xFF6C7BFF),
    error = ErrorRed,
    onError = Color.White,
    errorContainer = ErrorRedBg,
    background = RawDarkBackground,
    onBackground = RawTextWhite,
    surface = RawDarkSurface,
    onSurface = RawTextWhite,
    surfaceVariant = RawDarkSurfaceVariant,
    onSurfaceVariant = RawTextGray,
    outline = RawTextMuted,
    outlineVariant = Color(0xFF2A303C),
    inverseSurface = RawTextWhite,
    inverseOnSurface = RawDarkBackground,
    surfaceContainerLowest = Color(0xFF0A0C10),
    surfaceContainerLow = RawDarkSurface,
    surfaceContainer = RawDarkCard,
    surfaceContainerHigh = RawDarkCardElevated,
    surfaceContainerHighest = Color(0xFF282F3C)
)

private val FinTrackLightColorScheme = lightColorScheme(
    primary = FinTrackGreen,
    onPrimary = Color.White,
    primaryContainer = FinTrackGreenLight,
    onPrimaryContainer = FinTrackGreenDark,
    secondary = Color(0xFF3D8B74),
    onSecondary = Color.White,
    secondaryContainer = RawLightCardElevated,
    onSecondaryContainer = RawTextGrayDark,
    tertiary = Color(0xFF6C7BFF),
    error = ErrorRed,
    onError = Color.White,
    errorContainer = ErrorRedBg,
    background = RawLightBackground,
    onBackground = RawTextDark,
    surface = RawLightSurface,
    onSurface = RawTextDark,
    surfaceVariant = RawLightSurfaceVariant,
    onSurfaceVariant = RawTextGrayDark,
    outline = RawTextMutedDark,
    outlineVariant = Color(0xFFE5E7EB),
    inverseSurface = RawTextDark,
    inverseOnSurface = RawLightBackground,
    surfaceContainerLowest = Color(0xFFFFFFFF),
    surfaceContainerLow = RawLightSurface,
    surfaceContainer = RawLightCard,
    surfaceContainerHigh = RawLightCardElevated,
    surfaceContainerHighest = Color(0xFFE5E7EB)
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

@Composable
fun FinTrackTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) FinTrackDarkColorScheme else FinTrackLightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = FinTrackTypography,
        content = content
    )
}