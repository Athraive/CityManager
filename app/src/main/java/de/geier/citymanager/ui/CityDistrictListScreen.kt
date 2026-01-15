package de.geier.citymanager.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import de.geier.citymanager.data.entity.CityDistrictEntity
import de.geier.citymanager.ui.viewmodel.CityViewModel

@Composable
fun CityDistrictListScreen(
    cityId: String,
    cityViewModel: CityViewModel,
    onDistrictSelected: (String) -> Unit
) {
    // Flow liefert direkt den aktuellen Stand aus Room
    val districts by cityViewModel
        .districtsForCity(cityId)
        .collectAsState(initial = emptyList())

    if (districts.isEmpty()) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Für diese Stadt sind noch keine Stadtviertel angelegt.",
                style = MaterialTheme.typography.bodyLarge
            )
        }
        return
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(districts) { district ->
            CityDistrictListItem(
                district = district,
                onClick = { onDistrictSelected(district.id) }
            )
        }
    }
}

@Composable
private fun CityDistrictListItem(
    district: CityDistrictEntity,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = 8.dp)
    ) {
        Text(
            text = district.name,
            style = MaterialTheme.typography.titleMedium
        )
    }
}
