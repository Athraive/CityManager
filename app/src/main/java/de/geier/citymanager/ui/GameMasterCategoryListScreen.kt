@file:OptIn(ExperimentalMaterial3Api::class)

package de.geier.citymanager.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import de.geier.citymanager.ui.viewmodel.CityViewModel
import java.util.UUID

@Composable
fun GameMasterCategoryListScreen(
    categoryViewModel: PoiCategoryViewModel,
    cityViewModel: CityViewModel,
    allPois: List<PointOfInterest>
) {
    val categories by categoryViewModel.categories.collectAsState()
    val factions by cityViewModel.factions.collectAsState()

    var selectedCategory by remember { mutableStateOf<PoiCategory?>(null) }
    var selectedPoi by remember { mutableStateOf<PointOfInterest?>(null) }
    var editCategory by remember { mutableStateOf<PoiCategory?>(null) }

    /* ------------------------------------------------------------------ */
    /* POI DETAIL (SL)                                                     */
    /* ------------------------------------------------------------------ */

    if (selectedPoi != null) {
        PlayerPoiDetailScreen(
            poi = selectedPoi!!,
            factions = factions,
            isGameMaster = true,
            categoryTitle = selectedCategory?.title, // 👈 DAS FEHLTE
            onSave = { updated ->
                cityViewModel.savePoi(updated)
            },
            onDelete = { poi ->
                cityViewModel.deletePoi(poi)
            },
            onBack = { selectedPoi = null }
        )

        return
    }

    /* ------------------------------------------------------------------ */
    /* LIST / KATEGORIEN                                                   */
    /* ------------------------------------------------------------------ */

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    if (selectedCategory == null) {
                        // Neue Kategorie
                        categoryViewModel.save(
                            PoiCategory(
                                id = UUID.randomUUID().toString(),
                                title = "Neue Kategorie",
                                icon = "📁",
                                visible = true
                            )
                        )
                    } else {
                        // Neuer POI → DIREKT Detail-Screen
                        selectedPoi = PointOfInterest(
                            id = UUID.randomUUID().toString(),
                            name = "",
                            description = "",
                            categoryId = selectedCategory!!.id,
                            visible = true,
                            factionId = null,
                            playerNotes = "",
                            gameMasterNotes = "",
                            type = PoiType.LOCATION
                        )
                    }
                }
            ) {
                Icon(Icons.Default.Add, contentDescription = "Hinzufügen")
            }
        }
    ) { padding ->

        if (selectedCategory == null) {

            /* ---------------- Kategorien ---------------- */

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(categories) { category ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "${category.icon} ${category.title}",
                            modifier = Modifier
                                .weight(1f)
                                .clickable { selectedCategory = category }
                        )
                        Text(
                            text = "✏",
                            modifier = Modifier.clickable {
                                editCategory = category
                            }
                        )
                    }
                }
            }

        } else {

            /* ---------------- POIs ---------------- */

            val poisInCategory =
                allPois.filter { it.categoryId == selectedCategory!!.id }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
            ) {

                Text(
                    text = "← ${selectedCategory!!.title}",
                    modifier = Modifier
                        .padding(16.dp)
                        .clickable { selectedCategory = null }
                )

                LazyColumn(
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(poisInCategory) { poi ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { selectedPoi = poi }
                                .padding(vertical = 8.dp)
                        ) {
                            Text("• ${poi.name}")
                        }
                    }
                }
            }
        }
    }

    /* ------------------------------------------------------------------ */
    /* DIALOGE                                                            */
    /* ------------------------------------------------------------------ */

    editCategory?.let { category ->
        EditCategoryDialog(
            category = category,
            onDismiss = { editCategory = null },
            onSave = { updated ->
                categoryViewModel.save(updated)
                editCategory = null
            }
        )
    }
}

/* ====================================================================== */
/* DIALOGE                                                                */
/* ====================================================================== */

@Composable
private fun EditCategoryDialog(
    category: PoiCategory,
    onDismiss: () -> Unit,
    onSave: (PoiCategory) -> Unit
) {
    var title by remember { mutableStateOf(category.title) }
    var visible by remember { mutableStateOf(category.visible) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Kategorie bearbeiten") },
        text = {
            Column {
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Titel") }
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(
                        checked = visible,
                        onCheckedChange = { visible = it }
                    )
                    Text("Für Spieler sichtbar")
                }
            }
        },
        confirmButton = {
            Button(
                enabled = title.isNotBlank(),
                onClick = {
                    onSave(category.copy(title = title, visible = visible))
                }
            ) { Text("Speichern") }
        },
        dismissButton = {
            OutlinedButton(onClick = onDismiss) {
                Text("Abbrechen")
            }
        }
    )
}
