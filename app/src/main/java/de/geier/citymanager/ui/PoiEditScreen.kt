package de.geier.citymanager.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PoiEditScreen(
    poi: PointOfInterest,
    factions: List<Faction>,
    onSave: (PointOfInterest) -> Unit,
    onCancel: () -> Unit
) {
    var name by remember { mutableStateOf(poi.name) }
    var description by remember { mutableStateOf(poi.description) }
    var visible by remember { mutableStateOf(poi.visible) }

    // 🔹 Fraktionsauswahl
    var selectedFactionId by remember { mutableStateOf(poi.factionId) }
    var factionDropdownExpanded by remember { mutableStateOf(false) }

    val selectedFactionName =
        factions.firstOrNull { it.id == selectedFactionId }?.name ?: "Keine Fraktion"

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {

        Text(
            text = "✏ POI bearbeiten",
            style = MaterialTheme.typography.headlineSmall
        )

        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Name") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = description,
            onValueChange = { description = it },
            label = { Text("Beschreibung") },
            modifier = Modifier.fillMaxWidth()
        )

        // 🔹 Fraktions-Dropdown
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

        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Checkbox(
                checked = visible,
                onCheckedChange = { visible = it }
            )
            Text("Für Spieler sichtbar")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Button(
                enabled = name.isNotBlank(),
                onClick = {
                    onSave(
                        poi.copy(
                            name = name,
                            description = description,
                            visible = visible,
                            factionId = selectedFactionId
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
