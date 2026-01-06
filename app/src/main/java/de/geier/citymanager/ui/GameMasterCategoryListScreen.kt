package de.geier.citymanager.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun GameMasterCategoryListScreen(
    viewModel: PoiCategoryViewModel = viewModel()
) {
    val categories by viewModel.categories.collectAsState()

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(categories) { category ->
            GameMasterCategoryRow(category)
        }
    }
}

@Composable
private fun GameMasterCategoryRow(category: PoiCategory) {

    // 🔹 TEMPORÄR: Sichtbarkeit nur lokal
    var visibleForPlayers by remember { mutableStateOf(true) }

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = "${category.icon} ${category.title}",
                style = MaterialTheme.typography.titleMedium
            )
        }

        Checkbox(
            checked = visibleForPlayers,
            onCheckedChange = { visibleForPlayers = it }
        )
    }
}
