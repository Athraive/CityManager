package de.geier.citymanager.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import de.geier.citymanager.ui.viewmodel.CityViewModel
import de.geier.citymanager.ui.PoiCategoryViewModel
import de.geier.citymanager.ui.PoiCategoryViewModelFactory



@Composable
fun CategoryListScreen(
    categoryViewModel: PoiCategoryViewModel,
    cityViewModel: CityViewModel,
    factions: List<Faction>,
    accessContext: AccessContext
) {
    val categories by categoryViewModel.categories.collectAsState()

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp)
    ) {
        items(categories) { category ->
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        // Navigation kommt später – bewusst leer
                    }
                    .padding(vertical = 12.dp)
            ) {
                Text(
                    text = category.title,
                    style = MaterialTheme.typography.titleMedium
                )
            }
        }
    }
}
