package ru.kropotov.denet.test

import android.graphics.Color.TRANSPARENT
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import dagger.hilt.android.AndroidEntryPoint
import ru.kropotov.denet.test.compose.DenetApp
import ru.kropotov.denet.test.ui.theme.DenetTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val transparentStyle = SystemBarStyle.light(
            scrim = TRANSPARENT,
            darkScrim = TRANSPARENT
        )
        enableEdgeToEdge(
            statusBarStyle = transparentStyle,
            navigationBarStyle = transparentStyle
        )
        setContent {
            DenetTheme {
                DenetApp()
            }
        }
    }
}