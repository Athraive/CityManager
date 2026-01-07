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
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun PlayerCategoryListScreen(
    viewModel: PoiCategoryViewModel = viewModel(),
    allPois: List<PointOfInterest>
) {
    val categories by viewModel.categories.collectAsState()

    // 🔹 Spieler sehen nur sichtbare Kategorien
    val visibleCategories = remember(categories) {
        categories.filter { it.visible }
    }

    var selectedCategory by remember { mutableStateOf<PoiCategory?>(null) }

    if (selectedCategory == null) {
        // 🔹 Kategorienliste
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(visibleCategories) { category ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { selectedCategory = category }
                        .padding(12.dp)
                ) {
                    Text(
                        text = "${category.icon} ${category.title}",
                        style = MaterialTheme.typography.titleMedium
                    )
                }
            }
        }
    } else {
        // 🔹 POI-Liste innerhalb einer Kategorie
        val visiblePois = remember(allPois, selectedCategory) {
            allPois.filter {
                it.categoryId == selectedCategory!!.id && it.visible
            }
        }

        Column(modifier = Modifier.fillMaxSize()) {

            // ⬅ Zurück zur Kategorienliste
            Text(
                text = "← ${selectedCategory!!.title}",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier
                    .padding(16.dp)
                    .clickable { selectedCategory = null }
            )

            PlayerPoiListScreen(
                pois = visiblePois
            )
        }
    }
}
