package de.geier.citymanager.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import de.geier.citymanager.ui.data.POI_CATEGORIES

@Composable
fun PlayerCategoryListScreen(
    categories: List<PoiCategory> = POI_CATEGORIES
) {
    val visibleCategories = categories.filter { it.visible }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalAlignment = Alignment.Start
    ) {

        Text(
            text = "Kategorien",
            style = MaterialTheme.typography.headlineMedium
        )

        visibleCategories.forEach { category ->
            Text(
                text = "${category.icon}  ${category.title}",
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}
