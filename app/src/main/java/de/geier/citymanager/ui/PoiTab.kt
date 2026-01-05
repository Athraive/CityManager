package de.geier.citymanager.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun PoiTab() {
    Text(
        text = "Points of Interest\n(Kategorien folgen)",
        style = MaterialTheme.typography.headlineMedium,
        modifier = Modifier.padding(24.dp)
    )
}
