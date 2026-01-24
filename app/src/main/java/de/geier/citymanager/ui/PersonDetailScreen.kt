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
import de.geier.citymanager.ui.viewmodel.PersonViewModel

@Composable
fun PersonDetailScreen(
    person: Person,
    factions: List<Faction>,
    viewModel: PersonViewModel,
    onBack: () -> Unit,
    isGameMaster: Boolean = false,
    pois: List<PointOfInterest> = emptyList(),
    categories: List<PoiCategory> = emptyList(),
    onDelete: (Person) -> Unit = {},
    onOpenPoi: (String) -> Unit = {}
) {
    /* ---------------- lokaler Edit-State ---------------- */

    var name by remember(person.id) { mutableStateOf(person.name) }
    var description by remember(person.id) { mutableStateOf(person.description) }
    var playerNotes by remember(person.id) { mutableStateOf(person.playerNotes) }
    var gameMasterNotes by remember(person.id) { mutableStateOf(person.gameMasterNotes) }

    var selectedFactionId by remember(person.id) { mutableStateOf(person.factionId) }
    var visible by remember(person.id) { mutableStateOf(person.visible) }
    var factionDropdownExpanded by remember { mutableStateOf(false) }

    var showDeleteConfirm by remember { mutableStateOf(false) }

    val assignedPoiIds by viewModel.poiIdsForSelectedPerson.collectAsState()

    /* ---------------- POI-Sichtbarkeit ---------------- */

    val visibleCategoryIds = remember(categories) {
        categories.filter { it.visible }.map { it.id }.toSet()
    }

    val assignedPois = remember(pois, assignedPoiIds) {
        pois.filter { assignedPoiIds.contains(it.id) }
    }

    val visibleAssignedPois = remember(
        assignedPois,
        visibleCategoryIds,
        isGameMaster
    ) {
        if (isGameMaster) {
            assignedPois
        } else {
            assignedPois.filter { poi ->
                poi.visible && visibleCategoryIds.contains(poi.categoryId)
            }
        }
    }

    /* ---------------- UI ---------------- */

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(name.ifBlank { "Neue Person anlegen" })
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.Close, contentDescription = "Schließen")
                    }
                },
                actions = {
                    if (isGameMaster) {
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

            if (isGameMaster) {
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

            if (isGameMaster) {
                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 3
                )
            } else if (description.isNotBlank()) {
                Text(description, style = MaterialTheme.typography.bodyLarge)
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

            /* ---------- Fraktion ---------- */

            HorizontalDivider()
            Text("Fraktion", style = MaterialTheme.typography.titleMedium)

            if (isGameMaster) {
                val selectedFactionName =
                    factions.firstOrNull { it.id == selectedFactionId }?.name
                        ?: "Keine Fraktion"

                ExposedDropdownMenuBox(
                    expanded = factionDropdownExpanded,
                    onExpandedChange = { factionDropdownExpanded = !factionDropdownExpanded }
                ) {
                    OutlinedTextField(
                        value = selectedFactionName,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Fraktion") },
                        trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(
                                expanded = factionDropdownExpanded
                            )
                        },
                        modifier = Modifier.menuAnchor().fillMaxWidth()
                    )

                    ExposedDropdownMenu(
                        expanded = factionDropdownExpanded,
                        onDismissRequest = { factionDropdownExpanded = false }
                    ) {
                        DropdownMenuItem(
                            text = { Text("Keine Fraktion") },
                            onClick = {
                                selectedFactionId = null
                                factionDropdownExpanded = false
                            }
                        )
                        factions.forEach { faction ->
                            DropdownMenuItem(
                                text = { Text(faction.name) },
                                onClick = {
                                    selectedFactionId = faction.id
                                    factionDropdownExpanded = false
                                }
                            )
                        }
                    }
                }
            } else {
                factions.firstOrNull {
                    it.id == selectedFactionId && it.visible
                }?.let {
                    Text(it.name, style = MaterialTheme.typography.bodyLarge)
                }
            }

            /* ---------- Zugeordnete Orte ---------- */

            HorizontalDivider()
            Text("Orte", style = MaterialTheme.typography.titleMedium)

            if (visibleAssignedPois.isEmpty()) {
                Text(
                    text = "Keine Orte zugewiesen",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            } else {
                visibleAssignedPois.forEach { poi ->
                    Text(
                        text = "• ${poi.name}",
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.clickable {
                            onOpenPoi(poi.id)
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

            if (isGameMaster) {
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
                            factionId = selectedFactionId,
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
            text = {
                Text(
                    "Möchtest du die Person „${name.ifBlank { "ohne Namen" }}“ wirklich löschen?"
                )
            },
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
