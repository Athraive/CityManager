@file:OptIn(ExperimentalMaterial3Api::class)

package de.geier.citymanager.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun PlayerPoiDetailScreen(
    poi: PointOfInterest,
    factions: List<Faction>,
    onSave: (PointOfInterest) -> Unit,
    onBack: () -> Unit
) {
    /* ---------------- State ---------------- */

    var playerNotes by remember(poi.id) {
        mutableStateOf(poi.playerNotes)
    }

    /* ---------------- Fraktion (nur sichtbar) ---------------- */

    val faction = remember(factions, poi.factionId) {
        factions.firstOrNull {
            it.id == poi.factionId && it.visible
        }
    }

    /* ---------------- UI ---------------- */

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(poi.name) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Schließen"
                        )
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

            /* ---------- Beschreibung ---------- */

            if (poi.description.isNotBlank()) {
                Text(
                    text = "Beschreibung",
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = poi.description,
                    style = MaterialTheme.typography.bodyLarge
                )
            }

            /* ---------- Fraktion ---------- */

            faction?.let {
                HorizontalDivider()
                Text(
                    text = "Fraktion",
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = it.name,
                    style = MaterialTheme.typography.bodyLarge
                )
            }

            /* ---------- Notizen ---------- */

            HorizontalDivider()
            Text(
                text = "Notizen",
                style = MaterialTheme.typography.titleMedium
            )

            OutlinedTextField(
                value = playerNotes,
                onValueChange = { playerNotes = it },
                modifier = Modifier.fillMaxWidth(),
                minLines = 4,
                placeholder = {
                    Text("Deine Notizen zu diesem Ort …")
                }
            )

            /* ---------- Speichern ---------- */

            Button(
                onClick = {
                    onSave(
                        poi.copy(playerNotes = playerNotes)
                    )
                    onBack()
                }
            ) {
                Text("Speichern")
            }
        }
    }
}
