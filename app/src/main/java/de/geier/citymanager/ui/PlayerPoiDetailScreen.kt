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
fun PlayerPoiDetailScreen(
    poi: PointOfInterest,
    factions: List<Faction>,
    onBack: () -> Unit
) {
    /* ---------------- Fraktion (Spielersicht) ---------------- */

    val faction = remember(factions, poi.factionId) {
        factions.firstOrNull {
            it.id == poi.factionId && it.visible
        }
    }

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

            if (poi.description.isNotBlank()) {
                Text(
                    text = poi.description,
                    style = MaterialTheme.typography.bodyLarge
                )
            }

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

            /* ---------------- Spieler-Notizen ---------------- */

            if (poi.playerNotes.isNotBlank()) {
                HorizontalDivider()

                Text(
                    text = "Notizen",
                    style = MaterialTheme.typography.titleMedium
                )

                Text(
                    text = poi.playerNotes,
                    style = MaterialTheme.typography.bodyLarge
                )
            }

            /* ----------------
             * Personen absichtlich NICHT angezeigt
             *
             * Begründung:
             * - Personen ↔ POI ist ein internes SL-Werkzeug
             * - Spieler-UX soll aktuell nicht damit belastet werden
             * - Reaktivierung später problemlos möglich
             * ---------------- */
        }
    }
}
