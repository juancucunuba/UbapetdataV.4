package com.example.ubapetdata.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColorScheme = lightColorScheme(
    primary = TealPrimary,
    onPrimary = OnTeal,
    primaryContainer = AquaSurfaceVariant,
    onPrimaryContainer = TealDeep,
    secondary = TealSecondary,
    onSecondary = OnTeal,
    secondaryContainer = Color(0xFF99F6E4),
    onSecondaryContainer = TealDeep,
    tertiary = TealAccent,
    onTertiary = TealDeep,
    background = AquaBackground,
    onBackground = OnBackground,
    surface = AquaSurface,
    onSurface = OnBackground,
    surfaceVariant = AquaSurfaceVariant,
    onSurfaceVariant = OnSurfaceMuted,
    outline = TealSecondary,
    error = ConditionCritical,
    onError = OnTeal
)

private val DarkColorScheme = darkColorScheme(
    primary = TealAccent,
    onPrimary = TealDeep,
    primaryContainer = TealDark,
    onPrimaryContainer = AquaSurfaceVariant,
    secondary = TealSecondary,
    onSecondary = TealDeep,
    tertiary = TealPrimary,
    onTertiary = OnTeal,
    background = Color(0xFF042F2E),
    onBackground = AquaSurfaceVariant,
    surface = Color(0xFF0F3D3A),
    onSurface = AquaSurfaceVariant,
    surfaceVariant = TealDeep,
    onSurfaceVariant = Color(0xFF99F6E4),
    outline = TealSecondary,
    error = ConditionCritical,
    onError = OnTeal
)

@Composable
fun UbapetdataTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
