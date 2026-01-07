package de.geier.citymanager.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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

    var selectedCategory by remember { mutableStateOf<PoiCategory?>(null) }
    var editCategory by remember { mutableStateOf<PoiCategory?>(null) }
    var showCreatePoi by remember { mutableStateOf(false) }
    var editPoi by remember { mutableStateOf<PointOfInterest?>(null) }

    if (selectedCategory == null) {
        Column(modifier = Modifier.fillMaxSize()) {

            Button(
                onClick = {
                    categoryViewModel.save(
                        PoiCategory(
                            id = UUID.randomUUID().toString(),
                            title = "Neue Kategorie",
                            icon = "📁",
                            visible = true
                        )
                    )
                },
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
            ) {
                Text("➕ Kategorie hinzufügen")
            }

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
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
                            style = MaterialTheme.typography.titleMedium,
                            modifier = Modifier
                                .weight(1f)
                                .clickable { selectedCategory = category }
                        )

                        Text(
                            text = "✏",
                            modifier = Modifier
                                .padding(start = 8.dp)
                                .clickable { editCategory = category }
                        )
                    }
                }
            }
        }
    } else {
        val poisInCategory = allPois.filter { it.categoryId == selectedCategory!!.id }

        Column(modifier = Modifier.fillMaxSize()) {

            Text(
                text = "← ${selectedCategory!!.title}",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier
                    .padding(16.dp)
                    .clickable { selectedCategory = null }
            )

            Button(
                onClick = { showCreatePoi = true },
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .fillMaxWidth()
            ) {
                Text("➕ POI hinzufügen")
            }

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
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
                            modifier = Modifier
                                .padding(start = 8.dp)
                                .clickable { editPoi = poi }
                        )
                    }
                }
            }
        }
    }

    /* ---------- DIALOGE (EXPLIZIT HIER) ---------- */

    if (editCategory != null) {
        EditCategoryDialog(
            category = editCategory!!,
            onDismiss = { editCategory = null },
            onSave = {
                categoryViewModel.save(it)
                editCategory = null
            }
        )
    }

    if (showCreatePoi && selectedCategory != null) {
        CreatePoiDialog(
            categoryId = selectedCategory!!.id,
            onDismiss = { showCreatePoi = false },
            onSave = {
                cityViewModel.savePoi(it)
                showCreatePoi = false
            }
        )
    }

    if (editPoi != null) {
        EditPoiDialog(
            poi = editPoi!!,
            onDismiss = { editPoi = null },
            onSave = {
                cityViewModel.savePoi(it)
                editPoi = null
            }
        )
    }
}

/* ---------------- DIALOGE ---------------- */

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
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Name") }
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(checked = visible, onCheckedChange = { visible = it })
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
    var description by remember { mutableStateOf("") }
    var visible by remember { mutableStateOf(true) }

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
                            description = description,
                            categoryId = categoryId,
                            type = PoiType.LOCATION,
                            visible = visible
                        )
                    )
                }
            ) { Text("Speichern") }
        },
        dismissButton = {
            OutlinedButton(onClick = onDismiss) { Text("Abbrechen") }
        },
        title = { Text("POI anlegen") },
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
                    label = { Text("Beschreibung") }
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(checked = visible, onCheckedChange = { visible = it })
                    Text("Für Spieler sichtbar")
                }
            }
        }
    )
}

@Composable
private fun EditPoiDialog(
    poi: PointOfInterest,
    onDismiss: () -> Unit,
    onSave: (PointOfInterest) -> Unit
) {
    var name by remember { mutableStateOf(poi.name) }
    var description by remember { mutableStateOf(poi.description) }
    var visible by remember { mutableStateOf(poi.visible) }

    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            Button(
                enabled = name.isNotBlank(),
                onClick = {
                    onSave(poi.copy(name = name, description = description, visible = visible))
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
                    label = { Text("Beschreibung") }
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(checked = visible, onCheckedChange = { visible = it })
                    Text("Für Spieler sichtbar")
                }
            }
        }
    )
}
