@file:OptIn(ExperimentalMaterial3Api::class)

package de.geier.citymanager.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun PoiEditScreen(
    poi: PointOfInterest,
    factions: List<Faction>,
    onSave: (PointOfInterest) -> Unit,
    onCancel: () -> Unit
) {
    /* ---------------- State ---------------- */

    var name by remember(poi.id) { mutableStateOf(poi.name) }
    var description by remember(poi.id) { mutableStateOf(poi.description) }
    var visible by remember(poi.id) { mutableStateOf(poi.visible) }

    var gameMasterNotes by remember(poi.id) {
        mutableStateOf(poi.gameMasterNotes)
    }

    /* ---------------- Fraktion ---------------- */

    var selectedFactionId by remember(poi.id) {
        mutableStateOf(poi.factionId)
    }
    var factionDropdownExpanded by remember { mutableStateOf(false) }

    val selectedFactionName =
        factions.firstOrNull { it.id == selectedFactionId }?.name
            ?: "Keine Fraktion"

    /* ---------------- UI ---------------- */

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        Text(
            text = "POI bearbeiten",
            style = MaterialTheme.typography.headlineSmall
        )

        /* ---------- Basisdaten ---------- */

        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Name") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = description,
            onValueChange = { description = it },
            label = { Text("Öffentliche Beschreibung") },
            modifier = Modifier.fillMaxWidth(),
            minLines = 3
        )

        /* ---------- Fraktion ---------- */

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
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = factionDropdownExpanded)
                },
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth()
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

        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            Checkbox(
                checked = visible,
                onCheckedChange = { visible = it }
            )
            Text("Für Spieler sichtbar")
        }

        HorizontalDivider()

        /* ---------- Spieler-Notizen (read-only) ---------- */

        if (poi.playerNotes.isNotBlank()) {
            Text(
                text = "Spieler-Notizen",
                style = MaterialTheme.typography.titleMedium
            )

            OutlinedTextField(
                value = poi.playerNotes,
                onValueChange = {},
                readOnly = true,
                modifier = Modifier.fillMaxWidth(),
                minLines = 3
            )

            HorizontalDivider()
        }

        /* ---------- SL-Notizen ---------- */

        Text(
            text = "SL-Notizen",
            style = MaterialTheme.typography.titleMedium
        )

        OutlinedTextField(
            value = gameMasterNotes,
            onValueChange = { gameMasterNotes = it },
            modifier = Modifier.fillMaxWidth(),
            minLines = 4,
            placeholder = {
                Text("Interne Notizen, Plot-Hinweise, Geheimnisse …")
            }
        )

        Spacer(modifier = Modifier.height(8.dp))

        /* ---------- Aktionen ---------- */

        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            Button(
                enabled = name.isNotBlank(),
                onClick = {
                    onSave(
                        poi.copy(
                            name = name,
                            description = description,
                            visible = visible,
                            factionId = selectedFactionId,
                            gameMasterNotes = gameMasterNotes
                        )
                    )
                }
            ) {
                Text("Speichern")
            }

            OutlinedButton(onClick = onCancel) {
                Text("Abbrechen")
            }
        }
    }
}
