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
import androidx.compose.ui.unit.dp
import de.geier.citymanager.ui.viewmodel.PersonViewModel
import java.util.UUID

@Composable
fun PersonenTab(
    viewModel: PersonViewModel,
    factions: List<Faction> = emptyList(),
    pois: List<PointOfInterest>,
    categories: List<PoiCategory>,
    isGameMaster: Boolean
) {
    val allPersons by viewModel.persons.collectAsState()
    val visiblePersonsForPlayer by viewModel.visiblePersonsForPlayer.collectAsState()
    val selectedPerson by viewModel.selectedPerson.collectAsState()

    /* ---------------- Personenquelle ---------------- */

    val persons = remember(allPersons, visiblePersonsForPlayer, isGameMaster) {
        if (isGameMaster) allPersons else visiblePersonsForPlayer
    }

    /* ---------------- POI-Härtung ---------------- */

    val visibleCategoryIds = remember(categories) {
        categories.filter { it.visible }.map { it.id }.toSet()
    }

    val hardenedPois = remember(pois, visibleCategoryIds, isGameMaster) {
        if (isGameMaster) {
            pois
        } else {
            pois.filter { poi ->
                poi.visible && visibleCategoryIds.contains(poi.categoryId)
            }
        }
    }

    /* ---------------- DETAIL ---------------- */

    if (selectedPerson != null) {
        PersonDetailScreen(
            person = selectedPerson!!,
            factions = factions,
            viewModel = viewModel,
            onBack = { viewModel.clearSelection() },
            isGameMaster = isGameMaster,
            pois = hardenedPois,
            categories = categories,
            onDelete = { personToDelete ->
                viewModel.delete(personToDelete)
            }
        )
        return
    }

    /* ---------------- LISTE ---------------- */

    Scaffold(
        floatingActionButton = {
            if (isGameMaster) {
                FloatingActionButton(
                    onClick = {
                        viewModel.selectPerson(
                            Person(
                                id = UUID.randomUUID().toString(),
                                name = "",
                                description = "",
                                portraitImageUri = null,
                                visible = true,
                                factionId = null,
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

        if (persons.isEmpty()) {
            Text(
                text = "Keine Personen vorhanden",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier
                    .padding(padding)
                    .padding(24.dp)
            )
        } else {
            LazyColumn(
                modifier = Modifier
                    .padding(padding)
                    .padding(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(
                    items = persons,
                    key = { it.id }
                ) { person ->
                    val faction = factions.firstOrNull { it.id == person.factionId }
                    val showFaction =
                        faction != null && (isGameMaster || faction.visible)

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { viewModel.selectPerson(person) }
                            .padding(16.dp)
                    ) {
                        Text(
                            text = person.name.ifBlank { "Unbenannte Person" },
                            style = MaterialTheme.typography.titleMedium
                        )

                        if (showFaction) {
                            Text(
                                text = faction!!.name,
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }

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
