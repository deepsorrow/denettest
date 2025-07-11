package ru.kropotov.denet.test.ui.theme

import android.app.Activity
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val DarkColorScheme = darkColorScheme(
    primary = Purple80,
    secondary = PurpleGrey80,
    tertiary = Pink80,
    background = Color(0xFF171717),
    primaryContainer = Color(0xFFFFD600),
    onPrimaryContainer = Color(0xFF121212)
)

data class CustomColors(
    val accent: Color
)

val LocalCustomColors = staticCompositionLocalOf {
    CustomColors(
        accent = Color(0xFFFF602E)
    )
}

@Composable
fun DenetTheme(content: @Composable () -> Unit) {
    val colorScheme = DarkColorScheme
    val customColors = CustomColors(
        accent = Color(0xFFFF602E)
    )
    SetStatusBarIconsLight()

    CompositionLocalProvider(LocalCustomColors provides customColors) {
        MaterialTheme(
            colorScheme = colorScheme,
            content = content
        )
    }
}

@Suppress("DEPRECATION")
@Composable
fun SetStatusBarIconsLight() {
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = false
        }
    }
}