package de.geier.citymanager.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import de.geier.citymanager.ui.viewmodel.CityViewModel
import java.util.UUID

/**
 * Fraktionen mit zugeordneten POIs (read-only Ressourcen).
 */
@Composable
fun FraktionenTab(
    cityViewModel: CityViewModel,
    isGameMaster: Boolean
) {
    val allFactions by cityViewModel.factions.collectAsState()
    val allPois by cityViewModel.allPois.collectAsState()

    // Spieler sehen nur sichtbare Fraktionen
    val visibleFactions = remember(allFactions, isGameMaster) {
        if (isGameMaster) allFactions else allFactions.filter { it.visible }
    }

    Column(modifier = Modifier.padding(24.dp)) {

        Text(
            text = "Fraktionen",
            style = MaterialTheme.typography.headlineMedium
        )

        // ➕ Fraktion hinzufügen (nur Spielleiter)
        if (isGameMaster) {
            Button(
                onClick = {
                    cityViewModel.saveFaction(
                        Faction(
                            id = UUID.randomUUID().toString(),
                            name = "Neue Fraktion",
                            visible = true
                        )
                    )
                },
                modifier = Modifier.padding(top = 16.dp)
            ) {
                Text("Fraktion hinzufügen")
            }
        }

        LazyColumn(
            modifier = Modifier.padding(top = 16.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            items(visibleFactions) { faction ->

                val factionPois = remember(allPois, faction.id) {
                    allPois.filter { it.factionId == faction.id }
                }

                Column {
                    // 🔹 Fraktionskopf
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = faction.name,
                            style = MaterialTheme.typography.titleMedium,
                            modifier = Modifier.weight(1f)
                        )

                        if (isGameMaster) {
                            Checkbox(
                                checked = faction.visible,
                                onCheckedChange = { visible ->
                                    cityViewModel.saveFaction(
                                        faction.copy(visible = visible)
                                    )
                                }
                            )
                        }
                    }

                    // 🔹 Ressourcen (POIs)
                    if (factionPois.isEmpty()) {
                        Text(
                            text = "Keine zugeordneten POIs",
                            style = MaterialTheme.typography.bodySmall,
                            modifier = Modifier.padding(start = 16.dp, top = 4.dp)
                        )
                    } else {
                        factionPois.forEach { poi ->
                            Text(
                                text = "• ${poi.name}",
                                style = MaterialTheme.typography.bodyMedium,
                                modifier = Modifier.padding(start = 16.dp, top = 4.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}
