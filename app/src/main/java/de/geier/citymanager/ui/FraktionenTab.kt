@file:OptIn(ExperimentalMaterial3Api::class)

package de.geier.citymanager.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import java.util.UUID

@Composable
fun FraktionenTab(
    accessContext: AccessContext,
    factions: List<Faction>,
    onSave: (Faction) -> Unit,
    onDelete: (Faction) -> Unit
) {
    var activeFaction by remember { mutableStateOf<Faction?>(null) }

    activeFaction?.let { faction ->
        FactionDetailScreen(
            faction = faction,
            onBack = { activeFaction = null },
            accessContext = accessContext,
            onSave = onSave,
            onDelete = onDelete
        )
        return
    }

    Scaffold(
        floatingActionButton = {
            if (accessContext.canEdit()) {
                FloatingActionButton(
                    onClick = {
                        // 🔑 EINZIGE ENTSCHEIDENDE ZEILE
                        activeFaction = Faction(
                            id = UUID.randomUUID().toString(),
                            name = "",
                            description = null,
                            visible = true,
                            playerNotes = "",
                            gameMasterNotes = ""
                        )
                    }
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Fraktion anlegen")
                }
            }
        }
    ) { padding ->

        val visibleFactions =
            if (accessContext.canEdit()) factions
            else factions.filter { it.visible }

        LazyColumn(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(
                items = visibleFactions,
                key = { it.id }
            ) { faction ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = { activeFaction = faction }
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp))
                    {
                        Text(faction.name, style = MaterialTheme.typography.titleMedium)
                        faction.description?.let { Text(it) }
                    }
                }
            }
        }
    }
}
