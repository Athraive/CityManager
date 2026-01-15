package de.geier.citymanager.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import de.geier.citymanager.ui.viewmodel.CityViewModel

@Composable
fun FraktionenTab(
    cityViewModel: CityViewModel,
    isGameMaster: Boolean
) {
    val factions by cityViewModel.factions.collectAsState()

    var selectedFaction by remember { mutableStateOf<Faction?>(null) }

    val visibleFactions =
        if (isGameMaster) factions else factions.filter { it.visible }

    if (selectedFaction == null) {
        /* ---------- LISTE ---------- */

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(visibleFactions) { faction ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { selectedFaction = faction }
                        .padding(12.dp),
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
            }
        }

    } else {
        /* ---------- DETAIL ---------- */

        val faction = selectedFaction!!

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            Text(
                text = "← ${faction.name}",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.clickable { selectedFaction = null }
            )

            if (!faction.description.isNullOrBlank()) {
                Text(
                    text = faction.description,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}
