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

@Composable
fun FactionDetailScreen(
    faction: Faction,
    assignedPersons: List<Person>,              // 🔹 neu
    assignedPois: List<PointOfInterest>,       // 🔹 neu
    accessContext: AccessContext,
    onBack: () -> Unit,
    onSave: (Faction) -> Unit,
    onDelete: (Faction) -> Unit
) {

    var name by remember(faction.id) { mutableStateOf(faction.name) }
    var description by remember(faction.id) { mutableStateOf(faction.description ?: "") }
    var visible by remember(faction.id) { mutableStateOf(faction.visible) }

    var playerNotes by remember(faction.id) { mutableStateOf(faction.playerNotes) }
    var gameMasterNotes by remember(faction.id) { mutableStateOf(faction.gameMasterNotes) }

    var showDeleteConfirm by remember { mutableStateOf(false) }

    val canEdit = accessContext.canEdit()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(name.ifBlank { "Neue Fraktion" }) },
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

            /* ---------- Name ---------- */

            if (canEdit) {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Name") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
            } else {
                Text(name, style = MaterialTheme.typography.titleLarge)
            }

            /* ---------- Beschreibung ---------- */

            Text("Beschreibung", style = MaterialTheme.typography.titleMedium)

            if (canEdit) {
                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    minLines = 3,
                    modifier = Modifier.fillMaxWidth()
                )
            } else if (description.isNotBlank()) {
                Text(description)
            }

            /* ---------- Sichtbarkeit ---------- */

            if (canEdit) {
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    Checkbox(
                        checked = visible,
                        onCheckedChange = { visible = it }
                    )
                    Text("Für Spieler sichtbar")
                }
            }

            /* ---------- Zugewiesene Personen (Read-only) ---------- */

            HorizontalDivider()
            Text("Personen", style = MaterialTheme.typography.titleMedium)

            if (assignedPersons.isEmpty()) {
                Text(
                    "Keine Personen zugeordnet",
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            } else {
                assignedPersons.forEach { person ->
                    Text("• ${person.name}")
                }
            }

            /* ---------- Zugewiesene POIs (Read-only) ---------- */

            HorizontalDivider()
            Text("Orte", style = MaterialTheme.typography.titleMedium)

            if (assignedPois.isEmpty()) {
                Text(
                    "Keine Orte zugeordnet",
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            } else {
                assignedPois.forEach { poi ->
                    Text("• ${poi.name}")
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

            if (accessContext.canViewSlNotes()) {
                Text("SL-Notizen")
                OutlinedTextField(
                    value = gameMasterNotes,
                    onValueChange = { gameMasterNotes = it },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 4
                )
            }

            if (canEdit) {
                Button(
                    enabled = name.isNotBlank(),
                    onClick = {
                        onSave(
                            faction.copy(
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
    }

    if (showDeleteConfirm) {
        AlertDialog(
            onDismissRequest = { showDeleteConfirm = false },
            title = { Text("Fraktion löschen?") },
            text = {
                Text("Möchtest du diese Fraktion wirklich löschen?")
            },
            confirmButton = {
                Button(
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.error
                    ),
                    onClick = {
                        onDelete(faction)
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
