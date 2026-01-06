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

    var selectedCategory by remember { mutableStateOf<PoiCategory?>(null) }

    if (selectedCategory == null) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(categories) { category ->
                PlayerCategoryRow(
                    category = category,
                    onClick = { selectedCategory = category }
                )
            }
        }
    } else {
        PlayerPoiListScreen(
            category = selectedCategory!!,
            pois = allPois
        )
    }
}

@Composable
private fun PlayerCategoryRow(
    category: PoiCategory,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(12.dp)
    ) {
        Text(
            text = "${category.icon} ${category.title}",
            style = MaterialTheme.typography.titleMedium
        )
    }
}
