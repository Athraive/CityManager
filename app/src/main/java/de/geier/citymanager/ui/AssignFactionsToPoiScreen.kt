@file:OptIn(ExperimentalMaterial3Api::class)

package de.geier.citymanager.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import de.geier.citymanager.ui.viewmodel.PoiViewModel

@Composable
fun AssignFactionsToPoiScreen(
    poi: PointOfInterest,
    factions: List<Faction>,
    poiViewModel: PoiViewModel,
    onBack: () -> Unit
) {

    val assignedFactionIds by poiViewModel
        .factionIdsForSelectedPoi
        .collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Fraktionen zuweisen") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.Close, contentDescription = "Schließen")
                    }
                }
            )
        }
    ) { padding ->

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(factions) { faction ->

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Checkbox(
                        checked = assignedFactionIds.contains(faction.id),
                        onCheckedChange = {
                            poiViewModel.toggleFactionAssignment(faction.id)
                        }
                    )
                    Text(faction.name)
                }
            }
        }
    }
}
