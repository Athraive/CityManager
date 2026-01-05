package de.geier.citymanager.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun PersonenTab() {
    Text(
        text = "Personen\n(Inhalt folgt)",
        style = MaterialTheme.typography.headlineMedium,
        modifier = Modifier.padding(24.dp)
    )
}
