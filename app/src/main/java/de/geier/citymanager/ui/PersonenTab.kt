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
import androidx.compose.foundation.shape.RoundedCornerShape

@Composable
fun PersonenTab(
    viewModel: PersonViewModel,
    factions: List<Faction>,
    pois: List<PointOfInterest>,
    categories: List<PoiCategory>,
    accessContext: AccessContext,
    onFactionLinkClicked: (String) -> Unit,
    onPoiLinkClicked: (String) -> Unit,
    onShowOnMap: (String) -> Unit
) {

    val persons by viewModel.persons.collectAsState()
    val selectedPerson by viewModel.selectedPerson.collectAsState()

    val assignedPoiIds by viewModel.poiIdsForSelectedPerson.collectAsState()
    val assignedFactionIds by viewModel.factionIdsForSelectedPerson.collectAsState()

    var showAssignPois by remember { mutableStateOf(false) }
    var showAssignFactions by remember { mutableStateOf(false) }
    var searchQuery by remember { mutableStateOf("") }

    val filteredPersons = remember(persons, searchQuery) {
        if (searchQuery.isBlank()) persons
        else {
            val q = searchQuery.lowercase(Locale.getDefault())
            persons.filter { it.name.lowercase(Locale.getDefault()).contains(q) }
        }
    }

    val groupedPersons = remember(filteredPersons) {
        filteredPersons
            .sortedBy { it.name.lowercase(Locale.getDefault()) }
            .groupBy {
                it.name.firstOrNull()?.uppercaseChar()?.toString() ?: "#"
            }
            .toSortedMap()
    }

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

            val canEdit = accessContext.canEdit()

            val visibleCategoryIds = remember(categories) {
                categories.filter { it.visible }.map { it.id }.toSet()
            }

            val assignedPois = remember(
                pois,
                assignedPoiIds,
                visibleCategoryIds,
                canEdit
            ) {
                pois.filter { poi ->
                    assignedPoiIds.contains(poi.id) &&
                            (canEdit || (poi.visible && visibleCategoryIds.contains(poi.categoryId)))
                }
            }

            val assignedFactions = remember(
                factions,
                assignedFactionIds,
                canEdit
            ) {
                factions.filter { faction ->
                    assignedFactionIds.contains(faction.id) &&
                            (canEdit || faction.visible)
                }
            }

            PersonDetailScreen(
                person = selectedPerson!!,
                assignedPois = assignedPois,
                assignedFactions = assignedFactions,
                accessContext = accessContext,
                onBack = { viewModel.clearSelection() },
                onSave = { viewModel.save(it) },
                onDelete = { viewModel.delete(it) },
                onAssignPois = { showAssignPois = true },
                onAssignFactions = { showAssignFactions = true },
                onPersonClick = { },

                onPoiClick = { id ->
                    onPoiLinkClicked(id)
                },

                onFactionClick = { id ->
                    onFactionLinkClicked(id)
                },
                onShowOnMap = { id ->
                    onShowOnMap(id)

                    // 🔥 zweiter Trigger direkt aus dem Tab
                    onShowOnMap(id)
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

                                    Spacer(modifier = Modifier.height(20.dp))

                                    Surface(
                                        shape = RoundedCornerShape(12.dp),
                                        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f),
                                        modifier = Modifier.padding(horizontal = 16.dp)
                                    ) {

                                        Text(
                                            text = letter,
                                            style = MaterialTheme.typography.bodyMedium,
                                            fontWeight = FontWeight.SemiBold,
                                            modifier = Modifier.padding(
                                                horizontal = 12.dp,
                                                vertical = 4.dp
                                            )
                                        )
                                    }

                                    Spacer(modifier = Modifier.height(10.dp))
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