@file:OptIn(ExperimentalMaterial3Api::class)

package de.geier.citymanager.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import de.geier.citymanager.ui.components.AppTopBar

@Composable
fun CategoryListScreen(
    categories: List<PoiCategory>,
    isGameMaster: Boolean,
    onCategorySelected: (PoiCategory) -> Unit,
    onBack: () -> Unit
) {
    Scaffold(
        topBar = {
            AppTopBar(
                title = "Kategorien",
                isGameMaster = isGameMaster,
                onBack = onBack
            )
        }
    ) { padding ->

        if (categories.isEmpty()) {
            Text(
                text = "Keine Kategorien vorhanden",
                modifier = Modifier
                    .padding(padding)
                    .padding(24.dp)
            )
        } else {
            LazyColumn(
                modifier = Modifier
                    .padding(padding)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(categories) { category ->
                    Text(
                        text = "${category.icon} ${category.title}",
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onCategorySelected(category) }
                            .padding(12.dp)
                    )
                }
            }
        }
    }
}
