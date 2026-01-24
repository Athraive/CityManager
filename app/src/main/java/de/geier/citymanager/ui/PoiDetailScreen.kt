@file:OptIn(ExperimentalMaterial3Api::class)

package de.geier.citymanager.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import de.geier.citymanager.ui.viewmodel.PoiDetailViewModel

@Composable
fun PoiDetailScreen(
    poi: PointOfInterest,
    persons: List<Person>,
    viewModel: PoiDetailViewModel,
    onBack: () -> Unit,
    isGameMaster: Boolean,
    onDelete: (PointOfInterest) -> Unit
) {
    /* ---------------- lokaler Edit-State ---------------- */

    var name by remember(poi.id) { mutableStateOf(poi.name) }
    var description by remember(poi.id) { mutableStateOf(poi.description) }
    var visible by remember(poi.id) { mutableStateOf(poi.visible) }

    var showDeleteConfirm by remember { mutableStateOf(false) }

    /* ---------------- Auswahl setzen ---------------- */

    LaunchedEffect(poi.id) {
        viewModel.selectPoi(poi)
    }

    val assignedPersonIds by viewModel
        .personIdsForSelectedPoi
        .collectAsState()

    /* ---------------- UI ---------------- */

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(name.ifBlank { "Ort bearbeiten" }) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Schließen"
                        )
                    }
                },
                actions = {
                    if (isGameMaster) {
                        IconButton(onClick = { showDeleteConfirm = true }) {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = "Löschen"
                            )
                        }
                    }
                }
            )
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            /* ---------- Name ---------- */

            if (isGameMaster) {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Name") },
                    singleLine = true
                )
            } else {
                Text(name, style = MaterialTheme.typography.titleLarge)
            }

            /* ---------- Beschreibung ---------- */

            Text("Beschreibung", style = MaterialTheme.typography.titleMedium)

            if (isGameMaster) {
                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 3
                )
            } else if (description.isNotBlank()) {
                Text(description)
            }

            /* ---------- Sichtbarkeit ---------- */

            if (isGameMaster) {
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    Checkbox(
                        checked = visible,
                        onCheckedChange = { visible = it }
                    )
                    Text("Für Spieler sichtbar")
                }
            }

            /* ---------- Zugeordnete Personen ---------- */

            HorizontalDivider()
            Text("Zugeordnete Personen", style = MaterialTheme.typography.titleMedium)

            if (assignedPersonIds.isEmpty()) {
                Text("Keine Personen zugeordnet")
            } else {
                persons
                    .filter { assignedPersonIds.contains(it.id) }
                    .forEach { person ->
                        Text("• ${person.name}")
                    }
            }

            /* ---------- Speichern ---------- */

            if (isGameMaster) {
                Button(
                    enabled = name.isNotBlank(),
                    onClick = {
                        viewModel.save(
                            poi.copy(
                                name = name,
                                description = description,
                                visible = visible
                            )
                        )
                        onBack()
                    }
                ) {
                    Text("Speichern")
                }
            }
        }
    }

    /* ---------------- Delete Confirm ---------------- */

    if (showDeleteConfirm) {
        AlertDialog(
            onDismissRequest = { showDeleteConfirm = false },
            title = { Text("Ort löschen?") },
            text = {
                Text(
                    "Möchtest du den Ort „${name.ifBlank { "ohne Namen" }}“ wirklich löschen? " +
                            "Diese Aktion kann nicht rückgängig gemacht werden."
                )
            },
            confirmButton = {
                Button(
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.error
                    ),
                    onClick = {
                        onDelete(poi)
                        showDeleteConfirm = false
                        onBack()
                    }
                ) {
                    Text("Löschen")
                }
            },
            dismissButton = {
                OutlinedButton(onClick = { showDeleteConfirm = false }) {
                    Text("Abbrechen")
                }
            }
        )
    }
}
