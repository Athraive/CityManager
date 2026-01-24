package de.geier.citymanager.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import java.util.UUID

@Composable
fun FraktionenTab(
    isGameMaster: Boolean,
    factions: List<Faction>,
    onSave: (Faction) -> Unit,
    onDelete: (Faction) -> Unit
) {
    var selectedFaction by remember { mutableStateOf<Faction?>(null) }

    val visibleFactions =
        if (isGameMaster) factions else factions.filter { it.visible }

    if (selectedFaction == null) {

        Scaffold(
            floatingActionButton = {
                if (isGameMaster) {
                    FloatingActionButton(
                        onClick = {
                            selectedFaction = Faction(
                                id = UUID.randomUUID().toString(),
                                name = "",
                                description = null,
                                visible = true
                            )
                        }
                    ) {
                        Icon(Icons.Default.Add, contentDescription = "Fraktion anlegen")
                    }
                }
            }
        ) { padding ->

            LazyColumn(
                modifier = Modifier
                    .padding(padding)
                    .padding(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(visibleFactions) { faction ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { selectedFaction = faction }
                            .padding(16.dp),
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
                                onCheckedChange = {
                                    onSave(faction.copy(visible = it))
                                }
                            )
                        }
                    }
                }
            }
        }

    } else {

        FactionDetailScreen(
            faction = selectedFaction!!,
            isGameMaster = isGameMaster,
            onBack = { selectedFaction = null },
            onSave = onSave,
            onDelete = onDelete
        )
    }
}
