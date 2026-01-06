package de.geier.citymanager.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun PlayerPoiListScreen(
    category: PoiCategory,
    pois: List<PointOfInterest>
) {
    val filteredPois = remember(pois, category.id) {
        pois.filter { it.categoryId == category.id && it.visible }
    }

    Column(modifier = Modifier.fillMaxSize()) {

        Text(
            text = "${category.icon} ${category.title}",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(16.dp)
        )

        LazyColumn(
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(filteredPois) { poi ->
                PlayerPoiRow(poi)
            }
        }
    }
}

@Composable
private fun PlayerPoiRow(poi: PointOfInterest) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { /* Detail kommt später */ }
    ) {
        Text(
            text = poi.name,
            style = MaterialTheme.typography.titleMedium
        )

        if (poi.description.isNotBlank()) {
            Text(
                text = poi.description,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}
