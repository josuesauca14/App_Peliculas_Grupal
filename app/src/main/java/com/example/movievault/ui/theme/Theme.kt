package com.example.movievault.ui.theme

import android.app.Activity
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val LightColorScheme = lightColorScheme(
    primary = RoyalBlue,
    secondary = IndigoModern,
    tertiary = TextMuted,
    background = BgLight,
    surface = PureWhite,
    onPrimary = PureWhite,
    onSecondary = PureWhite,
    onTertiary = TextDark,
    onBackground = TextDark,
    onSurface = TextDark,
    outline = BorderLight,
    error = ErrorRed,
    secondaryContainer = AccentPink
)

@Composable
fun MovieVaultTheme(
    content: @Composable () -> Unit
) {
    val colorScheme = LightColorScheme
    val view = LocalView.current

    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.primary.toArgb() // Barra de estado en azul real
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = false
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
