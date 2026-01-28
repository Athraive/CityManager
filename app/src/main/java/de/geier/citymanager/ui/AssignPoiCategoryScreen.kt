@file:OptIn(ExperimentalMaterial3Api::class)

package de.geier.citymanager.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/**
 * Kategorie-Auswahl für POI-Zuweisung innerhalb des Personen-Tabs.
 *
 * - Kein NavHost
 * - Kein Save
 * - ❌ Abbrechen verlässt den Zuweisungsmodus komplett
 */
@Composable
fun AssignPoiCategoryScreen(
    categories: List<PoiCategory>,
    isGameMaster: Boolean,
    onCategorySelected: (categoryId: String) -> Unit,
    onCancel: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("POI-Kategorie wählen") },
                actions = {
                    IconButton(onClick = onCancel) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Abbrechen"
                        )
                    }
                }
            )
        }
    ) { padding ->

        if (categories.isEmpty()) {
            Text(
                text = "Keine Kategorien vorhanden",
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
                    items = categories,
                    key = { it.id }
                ) { category ->

                    // Spieler sehen nur sichtbare Kategorien
                    if (isGameMaster || category.visible) {
                        Surface(
                            tonalElevation = 1.dp,
                            shape = MaterialTheme.shapes.medium,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    onCategorySelected(category.id)
                                }
                        ) {
                            Column(
                                modifier = Modifier.padding(16.dp),
                                verticalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Text(
                                    text = category.title,
                                    style = MaterialTheme.typography.titleMedium
                                )

                                category.description
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
