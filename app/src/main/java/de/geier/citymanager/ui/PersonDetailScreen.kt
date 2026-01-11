package de.geier.citymanager.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import de.geier.citymanager.ui.viewmodel.PersonViewModel   // ✅ FIX

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PersonDetailScreen(
    person: Person,
    factions: List<Faction>,
    viewModel: PersonViewModel,
    onBack: () -> Unit,
    isGameMaster: Boolean = false,
    pois: List<PointOfInterest> = emptyList(),
    categories: List<PoiCategory> = emptyList()
) {
    var notes by remember(person.id) { mutableStateOf(person.sharedNotes) }
    var selectedFactionId by remember(person.id) { mutableStateOf(person.factionId) }
    var visible by remember(person.id) { mutableStateOf(person.visible) }
    var factionDropdownExpanded by remember { mutableStateOf(false) }

    val assignedPoiIds by viewModel.poiIdsForSelectedPerson.collectAsState()

    /* ---------------- POI-Härtung ---------------- */

    val visibleCategoryIds = remember(categories) {
        categories.filter { it.visible }.map { it.id }.toSet()
    }

    val visibleAssignedPois = remember(
        pois,
        assignedPoiIds,
        visibleCategoryIds,
        isGameMaster
    ) {
        if (isGameMaster) {
            pois.filter { assignedPoiIds.contains(it.id) }
        } else {
            pois.filter { poi ->
                poi.visible &&
                        assignedPoiIds.contains(poi.id) &&
                        visibleCategoryIds.contains(poi.categoryId)
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(person.name) },
                navigationIcon = {
                    Text(
                        text = "←",
                        modifier = Modifier
                            .padding(horizontal = 16.dp)
                            .clickable { onBack() }
                    )
                }
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

            Text(text = person.name, fontSize = 24.sp)

            if (!person.description.isNullOrBlank()) {
                Text(text = person.description)
            }

            if (isGameMaster) {
                HorizontalDivider()

                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    Checkbox(
                        checked = visible,
                        onCheckedChange = { visible = it }
                    )
                    Text("Für Spieler sichtbar")
                }
            }

            HorizontalDivider()
            Text("Fraktion", style = MaterialTheme.typography.titleMedium)

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
                        modifier = Modifier.menuAnchor().fillMaxWidth()
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
                factions.firstOrNull {
                    it.id == selectedFactionId && it.visible
                }?.let {
                    Text(it.name)
                }
            }

            if (isGameMaster && pois.isNotEmpty()) {
                HorizontalDivider()
                Text("Zugeordnete Orte (POIs)", style = MaterialTheme.typography.titleMedium)

                pois.forEach { poi ->
                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        Checkbox(
                            checked = assignedPoiIds.contains(poi.id),
                            onCheckedChange = { viewModel.togglePoiAssignment(poi.id) }
                        )
                        Text(poi.name)
                    }
                }
            }

            if (!isGameMaster && visibleAssignedPois.isNotEmpty()) {
                HorizontalDivider()
                Text("Orte", style = MaterialTheme.typography.titleMedium)

                visibleAssignedPois.forEach { poi ->
                    Text("• ${poi.name}")
                }
            }

            HorizontalDivider()
            Text("Notizen", style = MaterialTheme.typography.titleMedium)

            OutlinedTextField(
                value = notes,
                onValueChange = { notes = it },
                modifier = Modifier.fillMaxWidth(),
                minLines = 4
            )

            Button(
                onClick = {
                    viewModel.save(
                        person.copy(
                            sharedNotes = notes,
                            factionId = selectedFactionId,
                            visible = visible
                        )
                    )
                }
            ) {
                Text("Speichern")
            }
        }
    }
}
