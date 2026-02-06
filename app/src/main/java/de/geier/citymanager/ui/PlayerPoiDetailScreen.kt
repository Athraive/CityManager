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
fun PlayerPoiDetailScreen(
    poi: PointOfInterest,
    factions: List<Faction>,
    assignedFactionIds: Set<String>, // 👈 WICHTIG
    accessContext: AccessContext,
    onSave: (PointOfInterest) -> Unit,
    onDelete: (PointOfInterest) -> Unit = {},
    onBack: () -> Unit,
    categoryTitle: String? = null
) {
    /* ---------------- lokaler Edit-State ---------------- */

    var name by remember(poi.id) { mutableStateOf(poi.name) }
    var description by remember(poi.id) { mutableStateOf(poi.description ?: "") }
    var playerNotes by remember(poi.id) { mutableStateOf(poi.playerNotes) }
    var gameMasterNotes by remember(poi.id) { mutableStateOf(poi.gameMasterNotes) }
    var visible by remember(poi.id) { mutableStateOf(poi.visible) }

    var showDeleteConfirm by remember { mutableStateOf(false) }

    /* ---------------- zugeordnete & sichtbare Fraktionen ---------------- */

    val visibleAssignedFactions = remember(factions, assignedFactionIds) {
        factions.filter { faction ->
            faction.visible && assignedFactionIds.contains(faction.id)
        }
    }

    /* ---------------- UI ---------------- */

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        when {
                            name.isNotBlank() -> name
                            categoryTitle != null ->
                                "Neuen POI anlegen in $categoryTitle"
                            else -> "Neuer POI"
                        }
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.Close, contentDescription = "Schließen")
                    }
                },
                actions = {
                    if (accessContext.canEdit()) {
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

            if (accessContext.canEdit()) {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Name") },
                    singleLine = true
                )
            }

            /* ---------- Beschreibung ---------- */

            Text("Beschreibung", style = MaterialTheme.typography.titleMedium)

            if (accessContext.canEdit()) {
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

            if (accessContext.canEdit()) {
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    Checkbox(
                        checked = visible,
                        onCheckedChange = { visible = it }
                    )
                    Text("Für Spieler sichtbar")
                }
            }

            /* ---------- Fraktionen (read-only, korrekt gefiltert) ---------- */

            HorizontalDivider()
            Text("Fraktionen", style = MaterialTheme.typography.titleMedium)

            if (visibleAssignedFactions.isEmpty()) {
                Text(
                    "Keine Fraktionen zugeordnet",
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            } else {
                visibleAssignedFactions.forEach { faction ->
                    Text("• ${faction.name}")
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

            /* ---------- Speichern ---------- */

            if (accessContext.canEdit()) {
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
    }

    /* ---------------- Delete Confirm ---------------- */

    if (showDeleteConfirm) {
        AlertDialog(
            onDismissRequest = { showDeleteConfirm = false },
            title = { Text("POI löschen?") },
            text = {
                Text(
                    "Möchtest du den POI „${name.ifBlank { "Neuer POI" }}“ wirklich löschen?"
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
