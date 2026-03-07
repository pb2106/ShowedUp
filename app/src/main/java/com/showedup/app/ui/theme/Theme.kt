package com.showedup.app.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val DarkColorScheme = darkColorScheme(
    primary = Emerald500,
    onPrimary = Gray950,
    primaryContainer = Emerald700,
    onPrimaryContainer = Emerald400,
    secondary = Violet500,
    onSecondary = Gray950,
    secondaryContainer = Violet600,
    onSecondaryContainer = Violet400,
    tertiary = SignalGps,
    background = Gray950,
    onBackground = Gray100,
    surface = Gray900,
    onSurface = Gray100,
    surfaceVariant = Gray800,
    onSurfaceVariant = Gray400,
    surfaceContainerHighest = Gray800,
    surfaceContainerHigh = Gray850,
    surfaceContainer = Gray900,
    surfaceContainerLow = Gray900,
    surfaceContainerLowest = Gray950,
    outline = Gray700,
    outlineVariant = Gray800,
    error = ErrorRed,
    onError = White,
    errorContainer = Color(0xFF93000A),
    onErrorContainer = Color(0xFFFFDAD6)
)

private val LightColorScheme = lightColorScheme(
    primary = Emerald600,
    onPrimary = White,
    primaryContainer = Color(0xFFD1FAE5),
    onPrimaryContainer = Emerald700,
    secondary = Violet600,
    onSecondary = White,
    secondaryContainer = Color(0xFFEDE9FE),
    onSecondaryContainer = Violet600,
    tertiary = SignalGps,
    background = Gray50,
    onBackground = Gray900,
    surface = White,
    onSurface = Gray900,
    surfaceVariant = Gray100,
    onSurfaceVariant = Gray600,
    surfaceContainerHighest = Gray200,
    surfaceContainerHigh = Gray100,
    surfaceContainer = Gray50,
    surfaceContainerLow = OffWhite,
    surfaceContainerLowest = White,
    outline = Gray300,
    outlineVariant = Gray200,
    error = ErrorRed,
    onError = White,
    errorContainer = Color(0xFFFFDAD6),
    onErrorContainer = Color(0xFF93000A)
)

@Composable
fun ShowedUpTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            WindowCompat.setDecorFitsSystemWindows(window, false)
            window.statusBarColor = android.graphics.Color.TRANSPARENT
            window.navigationBarColor = android.graphics.Color.TRANSPARENT
            WindowCompat.getInsetsController(window, view).apply {
                isAppearanceLightStatusBars = !darkTheme
                isAppearanceLightNavigationBars = !darkTheme
            }
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = ShowedUpTypography,
        shapes = ShowedUpShapes,
        content = content
    )
}
