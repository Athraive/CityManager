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
    var editCategory by remember { mutableStateOf<PoiCategory?>(null) }
    var createPoiForCategory by remember { mutableStateOf<PoiCategory?>(null) }
    var editPoi by remember { mutableStateOf<PointOfInterest?>(null) }

    /* ---------------- Scaffold ---------------- */

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    if (selectedCategory == null) {
                        categoryViewModel.save(
                            PoiCategory(
                                id = UUID.randomUUID().toString(),
                                title = "Neue Kategorie",
                                icon = "📁",
                                visible = true
                            )
                        )
                    } else {
                        createPoiForCategory = selectedCategory
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
                            modifier = Modifier.clickable { editCategory = category }
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
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "• ${poi.name}",
                                modifier = Modifier.weight(1f)
                            )
                            Text(
                                text = "✏",
                                modifier = Modifier.clickable { editPoi = poi }
                            )
                        }
                    }
                }
            }
        }
    }

    /* ---------------- Dialoge ---------------- */

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

    createPoiForCategory?.let { category ->
        CreatePoiDialog(
            categoryId = category.id,
            onDismiss = { createPoiForCategory = null },
            onSave = { poi ->
                cityViewModel.savePoi(poi)
                createPoiForCategory = null
            }
        )
    }

    editPoi?.let { poi ->
        EditPoiDialog(
            poi = poi,
            factions = factions,
            onDismiss = { editPoi = null },
            onSave = { updated ->
                cityViewModel.savePoi(updated)
                editPoi = null
            }
        )
    }
}

/* ========================================================================== */
/*                                   DIALOGE                                  */
/* ========================================================================== */

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
        confirmButton = {
            Button(
                enabled = title.isNotBlank(),
                onClick = {
                    onSave(category.copy(title = title, visible = visible))
                }
            ) { Text("Speichern") }
        },
        dismissButton = {
            OutlinedButton(onClick = onDismiss) { Text("Abbrechen") }
        },
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
        }
    )
}

@Composable
private fun CreatePoiDialog(
    categoryId: String,
    onDismiss: () -> Unit,
    onSave: (PointOfInterest) -> Unit
) {
    var name by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            Button(
                enabled = name.isNotBlank(),
                onClick = {
                    onSave(
                        PointOfInterest(
                            id = UUID.randomUUID().toString(),
                            name = name,
                            categoryId = categoryId,
                            type = PoiType.LOCATION
                        )
                    )
                }
            ) { Text("Anlegen") }
        },
        dismissButton = {
            OutlinedButton(onClick = onDismiss) { Text("Abbrechen") }
        },
        title = { Text("POI anlegen") },
        text = {
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Name") }
            )
        }
    )
}

@Composable
private fun EditPoiDialog(
    poi: PointOfInterest,
    factions: List<Faction>,
    onDismiss: () -> Unit,
    onSave: (PointOfInterest) -> Unit
) {
    var name by remember { mutableStateOf(poi.name) }
    var description by remember { mutableStateOf(poi.description) }
    var playerNotes by remember { mutableStateOf(poi.playerNotes) }
    var gmNotes by remember { mutableStateOf(poi.gameMasterNotes) }
    var visible by remember { mutableStateOf(poi.visible) }
    var factionId by remember { mutableStateOf(poi.factionId) }
    var expanded by remember { mutableStateOf(false) }

    val factionName =
        factions.firstOrNull { it.id == factionId }?.name ?: "Keine Fraktion"

    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            Button(
                enabled = name.isNotBlank(),
                onClick = {
                    onSave(
                        poi.copy(
                            name = name,
                            description = description,
                            visible = visible,
                            factionId = factionId,
                            playerNotes = playerNotes,
                            gameMasterNotes = gmNotes
                        )
                    )
                }
            ) { Text("Speichern") }
        },
        dismissButton = {
            OutlinedButton(onClick = onDismiss) { Text("Abbrechen") }
        },
        title = { Text("POI bearbeiten") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {

                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Name") }
                )

                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Öffentliche Beschreibung") },
                    minLines = 3
                )

                ExposedDropdownMenuBox(
                    expanded = expanded,
                    onExpandedChange = { expanded = !expanded }
                ) {
                    OutlinedTextField(
                        value = factionName,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Fraktion") },
                        trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(expanded)
                        },
                        modifier = Modifier.menuAnchor().fillMaxWidth()
                    )

                    ExposedDropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        DropdownMenuItem(
                            text = { Text("Keine Fraktion") },
                            onClick = {
                                factionId = null
                                expanded = false
                            }
                        )
                        factions.forEach { faction ->
                            DropdownMenuItem(
                                text = { Text(faction.name) },
                                onClick = {
                                    factionId = faction.id
                                    expanded = false
                                }
                            )
                        }
                    }
                }

                OutlinedTextField(
                    value = playerNotes,
                    onValueChange = { playerNotes = it },
                    label = { Text("Spieler-Notizen") },
                    minLines = 3
                )

                OutlinedTextField(
                    value = gmNotes,
                    onValueChange = { gmNotes = it },
                    label = { Text("SL-Notizen (intern)") },
                    minLines = 3
                )

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(
                        checked = visible,
                        onCheckedChange = { visible = it }
                    )
                    Text("Für Spieler sichtbar")
                }
            }
        }
    )
}
