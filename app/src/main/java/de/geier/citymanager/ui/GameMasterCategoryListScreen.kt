package de.geier.citymanager.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun GameMasterCategoryListScreen() {

    val visibilityMap = remember {
        mutableStateMapOf<String, Boolean>().apply {
            DUMMY_POI_CATEGORIES.forEach { category ->
                this[category.id] = true
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {

        Text(
            text = "POI-Kategorien (Spielleiter)",
            style = MaterialTheme.typography.headlineMedium
        )

        DUMMY_POI_CATEGORIES.forEach { category ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {

                Text(
                    text = "${category.icon} ${category.title}",
                    modifier = Modifier.weight(1f),
                    style = MaterialTheme.typography.bodyLarge
                )

                Checkbox(
                    checked = visibilityMap[category.id] == true,
                    onCheckedChange = { checked ->
                        visibilityMap[category.id] = checked
                    }
                )
            }
        }
    }
}
