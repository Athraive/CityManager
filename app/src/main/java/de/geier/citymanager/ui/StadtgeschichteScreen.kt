package de.geier.citymanager.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import de.geier.citymanager.ui.viewmodel.CityViewModel

@Composable
fun StadtgeschichteScreen(
    cityId: String,
    cityViewModel: CityViewModel
) {
    val lore by cityViewModel.cityLore.collectAsState()

    if (lore == null) return

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = lore!!.title,
            style = MaterialTheme.typography.headlineMedium
        )

        Text(
            text = lore!!.text,
            style = MaterialTheme.typography.bodyLarge
        )
    }
}
