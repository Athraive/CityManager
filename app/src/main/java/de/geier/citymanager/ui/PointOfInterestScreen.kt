@file:OptIn(ExperimentalMaterial3Api::class)

package de.geier.citymanager.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun PointOfInterestScreen(
    poi: PointOfInterest,
    factions: List<Faction>,
    onSave: (PointOfInterest) -> Unit,
    onBack: () -> Unit
) {
    /* ---------------- State ---------------- */

    var description by remember(poi.id) { mutableStateOf(poi.description) }
    var gameMasterNotes by remember(poi.id) { mutableStateOf(poi.gameMasterNotes) }
    var visible by remember(poi.id) { mutableStateOf(poi.visible) }

    val faction = remember(factions, poi.factionId) {
        factions.firstOrNull { it.id == poi.factionId }
    }

    /* ---------------- UI ---------------- */

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(poi.name) },
                navigationIcon = {
                    Text(
                        text = "←",
                        modifier = Modifier
                            .padding(horizontal = 16.dp)
                            .clickable { onBack() }
                    )
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

            /* ---------------- Titel ---------------- */

            Text(
                text = poi.name,
                fontSize = 24.sp
            )

            /* ---------------- Öffentliche Beschreibung ---------------- */

            Text(
                text = "Öffentliche Beschreibung",
                style = MaterialTheme.typography.titleMedium
            )

            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                modifier = Modifier.fillMaxWidth(),
                minLines = 4,
                placeholder = { Text("Für Spieler sichtbare Beschreibung …") }
            )

            /* ---------------- Fraktion ---------------- */

            faction?.let {
                HorizontalDivider()

                Text(
                    text = "Fraktion",
                    style = MaterialTheme.typography.titleMedium
                )

                Text(
                    text = it.name,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            /* ---------------- Sichtbarkeit ---------------- */

            HorizontalDivider()

            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Checkbox(
                    checked = visible,
                    onCheckedChange = { visible = it }
                )
                Text("Für Spieler sichtbar")
            }

            /* ---------------- SL-Notizen ---------------- */

            HorizontalDivider()

            Text(
                text = "SL-Notizen",
                style = MaterialTheme.typography.titleMedium
            )

            OutlinedTextField(
                value = gameMasterNotes,
                onValueChange = { gameMasterNotes = it },
                modifier = Modifier.fillMaxWidth(),
                minLines = 6,
                placeholder = { Text("Interne Notizen, Plot, Geheimnisse …") }
            )

            /* ---------------- Aktionen ---------------- */

            Spacer(modifier = Modifier.height(12.dp))

            Button(
                onClick = {
                    onSave(
                        poi.copy(
                            description = description,
                            gameMasterNotes = gameMasterNotes,
                            visible = visible
                        )
                    )
                }
            ) {
                Text("Speichern")
            }
        }
    }
}
