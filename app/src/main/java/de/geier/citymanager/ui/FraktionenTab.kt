package de.geier.citymanager.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import de.geier.citymanager.ui.viewmodel.FactionViewModel
import de.geier.citymanager.ui.viewmodel.FactionViewModelFactory
import java.util.UUID

@Composable
fun FraktionenTab(
    isGameMaster: Boolean
) {
    /* ---------------- ViewModel ---------------- */

    val factionViewModel: FactionViewModel = viewModel(
        factory = FactionViewModelFactory()
    )

    val factions by factionViewModel.factions.collectAsState()

    /* ---------------- UI-State ---------------- */

    var selectedFaction by remember { mutableStateOf<Faction?>(null) }
    var showCreateDialog by remember { mutableStateOf(false) }

    val visibleFactions =
        if (isGameMaster) factions else factions.filter { it.visible }

    /* ---------------- LISTE ---------------- */

    if (selectedFaction == null) {

        Column(modifier = Modifier.fillMaxSize()) {

            if (isGameMaster) {
                Button(
                    onClick = { showCreateDialog = true },
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth()
                ) {
                    Text("➕ Fraktion anlegen")
                }
            }

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
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
                                    factionViewModel.save(
                                        faction.copy(visible = visible)
                                    )
                                }
                            )
                        }
                    }
                }
            }
        }

    } else {
        /* ---------------- DETAIL ---------------- */

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

    /* ---------------- CREATE DIALOG ---------------- */

    if (showCreateDialog) {
        CreateFactionDialog(
            onDismiss = { showCreateDialog = false },
            onCreate = { name, description ->
                factionViewModel.save(
                    Faction(
                        id = UUID.randomUUID().toString(),
                        name = name,
                        description = description,
                        visible = true
                    )
                )
                showCreateDialog = false
            }
        )
    }
}

/* ---------------- Dialog ---------------- */

@Composable
private fun CreateFactionDialog(
    onDismiss: () -> Unit,
    onCreate: (name: String, description: String?) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            Button(
                enabled = name.isNotBlank(),
                onClick = {
                    onCreate(
                        name.trim(),
                        description.takeIf { it.isNotBlank() }
                    )
                }
            ) {
                Text("Anlegen")
            }
        },
        dismissButton = {
            OutlinedButton(onClick = onDismiss) {
                Text("Abbrechen")
            }
        },
        title = { Text("Neue Fraktion") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Name*") },
                    singleLine = true
                )

                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Beschreibung") },
                    minLines = 3
                )
            }
        }
    )
}
