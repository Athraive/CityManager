@file:OptIn(ExperimentalMaterial3Api::class)

package de.geier.citymanager.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun AssignFactionsToPersonScreen(
    person: Person,
    factions: List<Faction>,
    initiallyAssignedFactionIds: Set<String>,
    onSave: (Set<String>) -> Unit,
    onBack: () -> Unit
) {
    /* ---------------- lokaler State ---------------- */

    var selectedFactionIds by remember {
        mutableStateOf(initiallyAssignedFactionIds)
    }

    /* ---------------- UI ---------------- */

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Fraktionen zuordnen") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.Close, contentDescription = "Abbrechen")
                    }
                },
                actions = {
                    IconButton(
                        onClick = {
                            onSave(selectedFactionIds)
                            onBack()
                        }
                    ) {
                        Icon(Icons.Default.Check, contentDescription = "Speichern")
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
                    .padding(padding)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(factions) { faction ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                selectedFactionIds =
                                    if (selectedFactionIds.contains(faction.id)) {
                                        selectedFactionIds - faction.id
                                    } else {
                                        selectedFactionIds + faction.id
                                    }
                            }
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Checkbox(
                            checked = selectedFactionIds.contains(faction.id),
                            onCheckedChange = { checked ->
                                selectedFactionIds =
                                    if (checked) {
                                        selectedFactionIds + faction.id
                                    } else {
                                        selectedFactionIds - faction.id
                                    }
                            }
                        )
                        Spacer(Modifier.width(12.dp))
                        Text(faction.name)
                    }
                }
            }
        }
    }
}
