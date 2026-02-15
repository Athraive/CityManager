package de.geier.citymanager.ui.theme

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.layout.Box

@Composable
fun CityThemeBackground() {

    val theme = LocalCityTheme.current

    val color = when (theme.backgroundPreset) {
        BackgroundPreset.WHITE -> Color.White
        BackgroundPreset.MEDIEVAL -> Color(0xFFF4EAD5)
        BackgroundPreset.SCIFI -> Color(0xFF0F2027)
        BackgroundPreset.WESTERN -> Color(0xFFD2B48C)
        BackgroundPreset.ASIA -> Color(0xFFE6E0D4)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color)
    )
}
