package de.geier.citymanager.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import de.geier.citymanager.ui.components.AppTopBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PersonDetailScreen(
    person: Person,
    factions: List<Faction>,
    viewModel: PersonViewModel,
    onBack: () -> Unit,
    isGameMaster: Boolean = false,
    pois: List<PointOfInterest> = emptyList()
) {
    var notes by remember(person.id) {
        mutableStateOf(person.sharedNotes)
    }

    var selectedFactionId by remember(person.id) {
        mutableStateOf(person.factionId)
    }
    var factionDropdownExpanded by remember { mutableStateOf(false) }

    val assignedPoiIds by viewModel.poiIdsForSelectedPerson.collectAsState()

    val visibleAssignedPois = remember(pois, assignedPoiIds) {
        pois.filter { it.visible && assignedPoiIds.contains(it.id) }
    }

    Scaffold(
        topBar = {
            AppTopBar(
                title = person.name,
                onBack = onBack
            )
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            Text(
                text = person.name,
                fontSize = 24.sp
            )

            if (!person.description.isNullOrBlank()) {
                Text(text = person.description)
            }

            /* ---------------- Fraktion ---------------- */

            HorizontalDivider()

            Text(
                text = "Fraktion",
                style = MaterialTheme.typography.titleMedium
            )

            if (isGameMaster) {

                val selectedFactionName =
                    factions.firstOrNull { it.id == selectedFactionId }?.name
                        ?: "Keine Fraktion"

                ExposedDropdownMenuBox(
                    expanded = factionDropdownExpanded,
                    onExpandedChange = { factionDropdownExpanded = !factionDropdownExpanded }
                ) {
                    OutlinedTextField(
                        value = selectedFactionName,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Fraktion") },
                        trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(
                                expanded = factionDropdownExpanded
                            )
                        },
                        modifier = Modifier
                            .menuAnchor()
                            .fillMaxWidth()
                    )

                    ExposedDropdownMenu(
                        expanded = factionDropdownExpanded,
                        onDismissRequest = { factionDropdownExpanded = false }
                    ) {
                        DropdownMenuItem(
                            text = { Text("Keine Fraktion") },
                            onClick = {
                                selectedFactionId = null
                                factionDropdownExpanded = false
                            }
                        )

                        factions.forEach { faction ->
                            DropdownMenuItem(
                                text = { Text(faction.name) },
                                onClick = {
                                    selectedFactionId = faction.id
                                    factionDropdownExpanded = false
                                }
                            )
                        }
                    }
                }

            } else {
                val visibleFaction = factions.firstOrNull {
                    it.id == selectedFactionId && it.visible
                }

                if (visibleFaction != null) {
                    Text(text = visibleFaction.name)
                }
            }

            /* ---------------- POIs ---------------- */

            if (isGameMaster) {

                if (pois.isNotEmpty()) {

                    HorizontalDivider()

                    Text(
                        text = "Zugeordnete Orte (POIs)",
                        style = MaterialTheme.typography.titleMedium
                    )

                    pois.forEach { poi ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Checkbox(
                                checked = assignedPoiIds.contains(poi.id),
                                onCheckedChange = {
                                    viewModel.togglePoiAssignment(poi.id)
                                }
                            )
                            Text(
                                text = poi.name,
                                modifier = Modifier.padding(top = 12.dp)
                            )
                        }
                    }
                }

            } else {

                if (visibleAssignedPois.isNotEmpty()) {

                    HorizontalDivider()

                    Text(
                        text = "Orte",
                        style = MaterialTheme.typography.titleMedium
                    )

                    visibleAssignedPois.forEach { poi ->
                        Text(
                            text = "• ${poi.name}",
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                }
            }

            /* ---------------- Notizen ---------------- */

            HorizontalDivider()

            Text(
                text = "Notizen",
                style = MaterialTheme.typography.titleMedium
            )

            OutlinedTextField(
                value = notes,
                onValueChange = { notes = it },
                modifier = Modifier.fillMaxWidth(),
                minLines = 4,
                label = { Text("Gemeinsame Notizen") }
            )

            Button(
                onClick = {
                    viewModel.save(
                        person.copy(
                            sharedNotes = notes,
                            factionId = selectedFactionId
                        )
                    )
                }
            ) {
                Text("Speichern")
            }
        }
    }
}
