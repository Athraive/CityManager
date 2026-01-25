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

enum class PersonTabMode {
    LIST,
    DETAIL,
    ASSIGN_POI_CATEGORY,
    ASSIGN_POIS
}

@Composable
fun PersonenTab(
    viewModel: PersonViewModel,
    factions: List<Faction>,
    pois: List<PointOfInterest>,
    categories: List<PoiCategory>,
    isGameMaster: Boolean
) {
    /* ---------------- ViewModel-State ---------------- */

    val allPersons by viewModel.persons.collectAsState()
    val visiblePersonsForPlayer by viewModel.visiblePersonsForPlayer.collectAsState()
    val selectedPerson by viewModel.selectedPerson.collectAsState()

    /* ---------------- UI-State ---------------- */

    var mode by remember { mutableStateOf(PersonTabMode.LIST) }
    var selectedPoiCategoryId by remember { mutableStateOf<String?>(null) }

    /* ---------------- Personenquelle ---------------- */

    val persons = remember(allPersons, visiblePersonsForPlayer, isGameMaster) {
        if (isGameMaster) allPersons else visiblePersonsForPlayer
    }

    /* ---------------- Modus-Synchronisation ---------------- */

    LaunchedEffect(selectedPerson) {
        if (selectedPerson == null) {
            mode = PersonTabMode.LIST
            selectedPoiCategoryId = null
        }
    }

    /* ======================================================
     * LISTE
     * ====================================================== */

    if (mode == PersonTabMode.LIST) {
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
                            mode = PersonTabMode.DETAIL
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
                                .clickable {
                                    viewModel.selectPerson(person)
                                    mode = PersonTabMode.DETAIL
                                }
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
        return
    }

    /* ======================================================
     * DETAIL
     * ====================================================== */

    if (mode == PersonTabMode.DETAIL && selectedPerson != null) {
        PersonDetailScreen(
            person = selectedPerson!!,
            factions = factions,
            viewModel = viewModel,
            onBack = {
                viewModel.clearSelection()
                mode = PersonTabMode.LIST
            },
            isGameMaster = isGameMaster,
            pois = pois,
            categories = categories,
            onDelete = {
                viewModel.delete(it)
                mode = PersonTabMode.LIST
            },
            onAssignPois = {
                if (isGameMaster) {
                    mode = PersonTabMode.ASSIGN_POI_CATEGORY
                }
            }
        )
        return
    }

    /* ======================================================
     * ASSIGN_POI_CATEGORY (Platzhalter)
     * ====================================================== */

    if (mode == PersonTabMode.ASSIGN_POI_CATEGORY && selectedPerson != null) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
        ) {
            Text("POI-Kategorien auswählen (kommt als Nächstes)")
        }
        return
    }

    /* ======================================================
     * ASSIGN_POIS (Platzhalter)
     * ====================================================== */

    if (mode == PersonTabMode.ASSIGN_POIS && selectedPerson != null) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
        ) {
            Text("POIs zuweisen (kommt als Nächstes)")
        }
        return
    }
}
