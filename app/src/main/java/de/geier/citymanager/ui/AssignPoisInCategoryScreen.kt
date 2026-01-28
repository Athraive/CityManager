@file:OptIn(ExperimentalMaterial3Api::class)

package de.geier.citymanager.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/**
 * POI-Zuweisung für genau eine Kategorie.
 *
 * - lokaler Edit-State
 * - 💾 Speichern bestätigt Änderungen
 * - ❌ Abbrechen verwirft Änderungen
 */
@Composable
fun AssignPoisInCategoryScreen(
    category: PoiCategory,
    poisInCategory: List<PointOfInterest>,
    initiallyAssignedPoiIds: Set<String>,
    isGameMaster: Boolean,
    onSave: (Set<String>) -> Unit,
    onCancel: () -> Unit
) {
    /* ---------------- lokaler Edit-State ---------------- */

    var selectedPoiIds by remember {
        mutableStateOf(initiallyAssignedPoiIds)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(category.title) },
                actions = {
                    TextButton(onClick = { onSave(selectedPoiIds) }) {
                        Text("Speichern")
                    }
                    IconButton(onClick = onCancel) {
                        Icon(
                            imageVector = Icons.Filled.Close,
                            contentDescription = "Abbrechen",
                            tint = MaterialTheme.colorScheme.error
                        )
                    }
                }
            )
        }
    ) { padding ->

        if (poisInCategory.isEmpty()) {
            Text(
                text = "Keine Orte in dieser Kategorie",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier
                    .padding(padding)
                    .padding(24.dp)
            )
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentPadding = PaddingValues(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(
                    items = poisInCategory,
                    key = { it.id }
                ) { poi ->

                    // Spieler sehen nur sichtbare POIs
                    if (isGameMaster || poi.visible) {
                        val checked = selectedPoiIds.contains(poi.id)

                        Surface(
                            shape = MaterialTheme.shapes.medium,
                            tonalElevation = 1.dp,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    selectedPoiIds =
                                        if (checked) {
                                            selectedPoiIds - poi.id
                                        } else {
                                            selectedPoiIds + poi.id
                                        }
                                }
                        ) {
                            Row(
                                modifier = Modifier
                                    .padding(16.dp)
                                    .fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                Checkbox(
                                    checked = checked,
                                    onCheckedChange = { isChecked ->
                                        selectedPoiIds =
                                            if (isChecked) {
                                                selectedPoiIds + poi.id
                                            } else {
                                                selectedPoiIds - poi.id
                                            }
                                    }
                                )

                                Column(
                                    verticalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    Text(
                                        text = poi.name,
                                        style = MaterialTheme.typography.titleMedium
                                    )

                                    poi.description
                                        ?.takeIf { it.isNotBlank() }
                                        ?.let { description ->
                                            Text(
                                                text = description,
                                                style = MaterialTheme.typography.bodySmall,
                                                color = MaterialTheme.colorScheme.onSurfaceVariant
                                            )
                                        }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
