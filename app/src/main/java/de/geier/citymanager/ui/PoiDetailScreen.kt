@file:OptIn(ExperimentalMaterial3Api::class)

package de.geier.citymanager.ui

import androidx.compose.foundation.clickable
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

@Composable
fun PoiDetailScreen(
    poi: PointOfInterest,
    assignedFactions: List<Faction>,
    assignedPersons: List<Person>,
    accessContext: AccessContext,
    onBack: () -> Unit,
    onSave: (PointOfInterest) -> Unit,
    onDelete: (PointOfInterest) -> Unit,
    onAssignFactions: () -> Unit,
    onPersonClick: (String) -> Unit,
    onFactionClick: (String) -> Unit
) {

    var name by remember(poi.id) { mutableStateOf(poi.name) }
    var description by remember(poi.id) { mutableStateOf(poi.description ?: "") }
    var visible by remember(poi.id) { mutableStateOf(poi.visible) }
    var playerNotes by remember(poi.id) { mutableStateOf(poi.playerNotes) }
    var gameMasterNotes by remember(poi.id) { mutableStateOf(poi.gameMasterNotes) }

    var showDeleteConfirm by remember { mutableStateOf(false) }

    val canEdit = accessContext.canEdit()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(name.ifBlank { "Ort" }) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.Close, contentDescription = "Schließen")
                    }
                },
                actions = {
                    if (canEdit) {
                        IconButton(onClick = { showDeleteConfirm = true }) {
                            Icon(Icons.Default.Delete, contentDescription = "Löschen")
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

            if (canEdit) {
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

            Text("Beschreibung", style = MaterialTheme.typography.titleMedium)

            if (canEdit) {
                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 3
                )
            } else if (description.isNotBlank()) {
                Text(description)
            }

            if (canEdit) {
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    Checkbox(
                        checked = visible,
                        onCheckedChange = { visible = it }
                    )
                    Text("Für Spieler sichtbar")
                }
            }

            /* ---------- Fraktionen ---------- */

            HorizontalDivider()
            Text("Fraktionen", style = MaterialTheme.typography.titleMedium)

            if (assignedFactions.isEmpty()) {
                Text(
                    "Keine Fraktionen zugeordnet",
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            } else {
                assignedFactions.forEach { faction ->
                    Text(
                        text = "• ${faction.name}",
                        modifier = Modifier.clickable {
                            onFactionClick(faction.id)
                        }
                    )
                }
            }

            if (canEdit) {
                Button(onClick = onAssignFactions) {
                    Text("Fraktionen zuweisen")
                }
            }

            /* ---------- Personen ---------- */

            HorizontalDivider()
            Text("Personen", style = MaterialTheme.typography.titleMedium)

            if (assignedPersons.isEmpty()) {
                Text(
                    "Keine Personen zugeordnet",
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            } else {
                assignedPersons.forEach { person ->
                    Text(
                        text = "• ${person.name}",
                        modifier = Modifier.clickable {
                            onPersonClick(person.id)
                        }
                    )
                }
            }

            /* ---------- Notizen ---------- */

            HorizontalDivider()
            Text("Notizen", style = MaterialTheme.typography.titleMedium)

            Text("Spieler-Notizen")
            OutlinedTextField(
                value = playerNotes,
                onValueChange = { playerNotes = it },
                modifier = Modifier.fillMaxWidth(),
                minLines = 4
            )

            if (canEdit) {
                Text("SL-Notizen")
                OutlinedTextField(
                    value = gameMasterNotes,
                    onValueChange = { gameMasterNotes = it },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 4
                )
            }

            Button(
                enabled = name.isNotBlank(),
                onClick = {
                    onSave(
                        poi.copy(
                            name = name,
                            description = description.takeIf { it.isNotBlank() },
                            visible = visible,
                            playerNotes = playerNotes,
                            gameMasterNotes = gameMasterNotes
                        )
                    )
                    onBack()
                }
            ) {
                Text("Speichern")
            }
        }
    }

    if (showDeleteConfirm) {
        AlertDialog(
            onDismissRequest = { showDeleteConfirm = false },
            title = { Text("Ort löschen?") },
            text = {
                Text("Möchtest du den Ort wirklich löschen?")
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
