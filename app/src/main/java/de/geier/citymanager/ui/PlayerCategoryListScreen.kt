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
import de.geier.citymanager.ui.viewmodel.CityViewModel

@Composable
fun PlayerCategoryListScreen(
    cityViewModel: CityViewModel,
    categoryViewModel: PoiCategoryViewModel,
    allPois: List<PointOfInterest>
) {
    val categories by categoryViewModel.categories.collectAsState()

    var selectedCategory by remember { mutableStateOf<PoiCategory?>(null) }
    var selectedPoi by remember { mutableStateOf<PointOfInterest?>(null) }

    /* ---------- KATEGORIEN ---------- */
    if (selectedCategory == null) {

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(categories.filter { it.visible }) { category ->
                Text(
                    text = "${category.icon} ${category.title}",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { selectedCategory = category }
                        .padding(12.dp)
                )
            }
        }

        /* ---------- POI-LISTE ---------- */
    } else if (selectedPoi == null) {

        val poisInCategory =
            allPois.filter {
                it.categoryId == selectedCategory!!.id && it.visible
            }

        Column(modifier = Modifier.fillMaxSize()) {

            Text(
                text = "← ${selectedCategory!!.title}",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier
                    .padding(16.dp)
                    .clickable { selectedCategory = null }
            )

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(poisInCategory) { poi ->
                    Text(
                        text = "• ${poi.name}",
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { selectedPoi = poi }
                            .padding(8.dp)
                    )
                }
            }
        }

        /* ---------- POI-DETAIL (neu, sauber) ---------- */
    } else {

        PlayerPoiDetailScreen(
            poi = selectedPoi!!,
            persons = emptyList(),              // 🔹 kommt im nächsten Schritt
            assignedPersonIds = emptySet(),     // 🔹 kommt im nächsten Schritt
            onBack = { selectedPoi = null }
        )
    }
}
