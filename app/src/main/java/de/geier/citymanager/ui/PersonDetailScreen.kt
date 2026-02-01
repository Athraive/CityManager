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
import de.geier.citymanager.ui.viewmodel.PersonViewModel

@Composable
fun PersonDetailScreen(
    person: Person,
    factions: List<Faction>,
    viewModel: PersonViewModel,
    onBack: () -> Unit,
    accessContext: AccessContext,
    pois: List<PointOfInterest>,
    categories: List<PoiCategory>,
    onDelete: (Person) -> Unit,
    onAssignPois: () -> Unit,
    onAssignFactions: () -> Unit
) {
    /* ---------------- lokaler Edit-State ---------------- */

    var name by remember(person.id) { mutableStateOf(person.name) }
    var description by remember(person.id) { mutableStateOf(person.description) }
    var playerNotes by remember(person.id) { mutableStateOf(person.playerNotes) }
    var gameMasterNotes by remember(person.id) { mutableStateOf(person.gameMasterNotes) }
    var visible by remember(person.id) { mutableStateOf(person.visible) }

    var showDeleteConfirm by remember { mutableStateOf(false) }

    val canEdit = accessContext.canEdit()

    /* ---------------- Zuweisungen ---------------- */

    val assignedPoiIds by viewModel.poiIdsForSelectedPerson.collectAsState()
    val assignedFactionIds by viewModel.factionIdsForSelectedPerson.collectAsState()

    val visibleCategoryIds = remember(categories) {
        categories.filter { it.visible }.map { it.id }.toSet()
    }

    val visiblePois = remember(pois, assignedPoiIds, visibleCategoryIds, canEdit) {
        pois.filter { poi ->
            assignedPoiIds.contains(poi.id) &&
                    (canEdit || (poi.visible && visibleCategoryIds.contains(poi.categoryId)))
        }
    }

    val visibleFactions = remember(factions, assignedFactionIds, canEdit) {
        factions.filter { faction ->
            assignedFactionIds.contains(faction.id) &&
                    (canEdit || faction.visible)
        }
    }

    /* ---------------- UI ---------------- */

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(name.ifBlank { "Person" }) },
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
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
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
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 3
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

            /* ---------- Fraktionen ---------- */

            HorizontalDivider()
            Text("Fraktionen", style = MaterialTheme.typography.titleMedium)

            if (visibleFactions.isEmpty()) {
                Text(
                    "Keine Fraktionen zugewiesen",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            } else {
                visibleFactions.forEach { faction ->
                    Text("• ${faction.name}")
                }
            }

            if (canEdit) {
                Button(onClick = onAssignFactions) {
                    Text("Fraktionen zuweisen")
                }
            }

            /* ---------- Orte ---------- */

            HorizontalDivider()
            Text("Orte", style = MaterialTheme.typography.titleMedium)

            if (visiblePois.isEmpty()) {
                Text(
                    "Keine Orte zugewiesen",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            } else {
                visiblePois.forEach { poi ->
                    Text("• ${poi.name}")
                }
            }

            if (canEdit) {
                Button(onClick = onAssignPois) {
                    Text("Orte zuweisen")
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

            /* ---------- Speichern ---------- */

            Button(
                enabled = name.isNotBlank(),
                onClick = {
                    viewModel.save(
                        person.copy(
                            name = name,
                            description = description,
                            playerNotes = playerNotes,
                            gameMasterNotes = gameMasterNotes,
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

    /* ---------------- Delete Confirm ---------------- */

    if (showDeleteConfirm) {
        AlertDialog(
            onDismissRequest = { showDeleteConfirm = false },
            title = { Text("Person löschen?") },
            text = { Text("Möchtest du diese Person wirklich löschen?") },
            confirmButton = {
                Button(
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.error
                    ),
                    onClick = {
                        onDelete(person)
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
