package com.example.pushistiyhvost.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColors = lightColorScheme(
    primary = Color(0xFF6F4AE6),
    secondary = Color(0xFFF6C542),
    background = Color(0xFFF5F1FA),
    surface = Color(0xFFFFFFFF),
    onPrimary = Color.White,
    onSecondary = Color(0xFF1F1F1F),
    onBackground = Color(0xFF26233A),
    onSurface = Color(0xFF26233A)
)

@Composable
fun PushistiyHvostTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = LightColors,
        content = content
    )
}