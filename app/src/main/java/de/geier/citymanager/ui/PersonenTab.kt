@file:OptIn(ExperimentalMaterial3Api::class)

package de.geier.citymanager.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import de.geier.citymanager.ui.viewmodel.PersonViewModel
import java.util.Locale
import java.util.UUID

@Composable
fun PersonenTab(
    viewModel: PersonViewModel,
    factions: List<Faction>,
    pois: List<PointOfInterest>,
    categories: List<PoiCategory>,
    accessContext: AccessContext,
    onFactionLinkClicked: () -> Unit
) {
    /* ---------------- State ---------------- */

    val persons by viewModel.persons.collectAsState()
    val selectedPerson by viewModel.selectedPerson.collectAsState()

    var showAssignPois by remember { mutableStateOf(false) }
    var showAssignFactions by remember { mutableStateOf(false) }
    var searchQuery by remember { mutableStateOf("") }

    /* ---------------- Suche ---------------- */

    val filteredPersons = remember(persons, searchQuery) {
        if (searchQuery.isBlank()) {
            persons
        } else {
            val q = searchQuery.lowercase(Locale.getDefault())
            persons.filter {
                it.name.lowercase(Locale.getDefault()).contains(q)
            }
        }
    }

    /* ---------------- Gruppierung ---------------- */

    val groupedPersons = remember(filteredPersons) {
        filteredPersons
            .sortedBy { it.name.lowercase(Locale.getDefault()) }
            .groupBy {
                it.name.firstOrNull()?.uppercaseChar()?.toString() ?: "#"
            }
            .toSortedMap()
    }

    /* ---------------- Navigation ---------------- */

    when {
        selectedPerson != null && showAssignPois -> {
            AssignPoisToPersonScreen(
                person = selectedPerson!!,
                pois = pois,
                viewModel = viewModel,
                onClose = { showAssignPois = false }
            )
        }

        selectedPerson != null && showAssignFactions -> {
            AssignFactionsToPersonScreen(
                factions = factions,
                viewModel = viewModel,
                onClose = { showAssignFactions = false }
            )
        }

        selectedPerson != null -> {
            PersonDetailScreen(
                person = selectedPerson!!,
                factions = factions,
                viewModel = viewModel,
                onBack = { viewModel.clearSelection() },
                accessContext = accessContext,
                pois = pois,
                categories = categories,
                onDelete = { viewModel.delete(it) },
                onAssignPois = { showAssignPois = true },
                onAssignFactions = { showAssignFactions = true },
                onFactionClick = {
                    onFactionLinkClicked()
                }
            )
        }

        else -> {
            Scaffold(
                floatingActionButton = {
                    if (accessContext.canEdit()) {
                        FloatingActionButton(
                            onClick = {
                                viewModel.selectPerson(
                                    Person(
                                        id = UUID.randomUUID().toString(),
                                        name = "",
                                        description = "",
                                        portraitImageUri = null,
                                        visible = true,
                                        playerNotes = "",
                                        gameMasterNotes = ""
                                    )
                                )
                            }
                        ) {
                            Icon(Icons.Default.Add, contentDescription = "Person anlegen")
                        }
                    }
                }
            ) { padding ->

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding)
                ) {

                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = { searchQuery = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        label = { Text("Person suchen") },
                        singleLine = true
                    )

                    if (groupedPersons.isEmpty()) {
                        Text(
                            text = "Keine Personen gefunden",
                            modifier = Modifier.padding(24.dp)
                        )
                    } else {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            contentPadding = PaddingValues(bottom = 16.dp)
                        ) {
                            groupedPersons.forEach { (letter, personsInGroup) ->

                                item {
                                    Text(
                                        text = letter,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(
                                                horizontal = 16.dp,
                                                vertical = 8.dp
                                            ),
                                        fontWeight = FontWeight.Bold,
                                        style = MaterialTheme.typography.titleSmall
                                    )
                                }

                                items(personsInGroup, key = { it.id }) { person ->
                                    Column(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clickable {
                                                viewModel.selectPerson(person)
                                            }
                                            .padding(
                                                horizontal = 16.dp,
                                                vertical = 12.dp
                                            )
                                    ) {
                                        Text(
                                            text = person.name.ifBlank { "Unbenannte Person" },
                                            style = MaterialTheme.typography.titleMedium
                                        )

                                        if (person.description.isNotBlank()) {
                                            Text(
                                                text = person.description,
                                                style = MaterialTheme.typography.bodyMedium,
                                                maxLines = 2
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
