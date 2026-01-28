@file:OptIn(ExperimentalMaterial3Api::class)

package de.geier.citymanager.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import de.geier.citymanager.ui.viewmodel.PersonViewModel

@Composable
fun AssignFactionsToPersonScreen(
    factions: List<Faction>,
    viewModel: PersonViewModel,
    onClose: () -> Unit
) {
    val assignedFactionIds by viewModel
        .factionIdsForSelectedPerson
        .collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Fraktionen zuweisen") },
                navigationIcon = {
                    IconButton(onClick = onClose) {
                        Icon(Icons.Default.Close, contentDescription = "Schließen")
                    }
                }
            )
        }
    ) { padding ->

        if (factions.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                Text("Keine Fraktionen vorhanden")
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(
                    items = factions,
                    key = { it.id }
                ) { faction ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Checkbox(
                            checked = assignedFactionIds.contains(faction.id),
                            onCheckedChange = {
                                viewModel.toggleFactionAssignment(faction.id)
                            }
                        )

                        Column {
                            Text(
                                text = faction.name,
                                style = MaterialTheme.typography.bodyLarge
                            )

                            faction.description
                                ?.takeIf { it.isNotBlank() }
                                ?.let { description ->
                                    Text(
                                        text = description,
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                        }
                    }
                }
            }
        }
    }
}
