package de.geier.citymanager.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.toggleable
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import de.geier.citymanager.ui.viewmodel.CityViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ManagePinsScreen(
    navController: NavController,
    cityViewModel: CityViewModel
) {

    val persons by cityViewModel.allPersons.collectAsState()
    val pois by cityViewModel.allPois.collectAsState()

    var selectedTab by remember { mutableStateOf(0) }
    var selectedIds by remember { mutableStateOf(setOf<String>()) }
    var confirmDelete by remember { mutableStateOf(false) }

    val personPins: List<Person> =
        persons.filter { it.mapX != null && it.mapY != null }

    val poiPins: List<PointOfInterest> =
        pois.filter { it.mapX != null && it.mapY != null }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Pins verwalten") }
            )
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ) {

            TabRow(selectedTabIndex = selectedTab) {

                Tab(
                    selected = selectedTab == 0,
                    onClick = {
                        selectedTab = 0
                        selectedIds = emptySet()
                    },
                    text = { Text("Personen") }
                )

                Tab(
                    selected = selectedTab == 1,
                    onClick = {
                        selectedTab = 1
                        selectedIds = emptySet()
                    },
                    text = { Text("POIs") }
                )
            }

            Spacer(Modifier.height(16.dp))

            if (selectedTab == 0) {

                PinsList(
                    items = personPins,
                    selectedIds = selectedIds,
                    onSelectionChange = { selectedIds = it }
                )

            } else {

                PinsList(
                    items = poiPins,
                    selectedIds = selectedIds,
                    onSelectionChange = { selectedIds = it }
                )
            }

            Spacer(Modifier.height(16.dp))

            Button(
                onClick = { confirmDelete = true },
                enabled = selectedIds.isNotEmpty(),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text("Ausgewählte entfernen")
            }
        }
    }

    if (confirmDelete) {

        AlertDialog(
            onDismissRequest = { confirmDelete = false },
            title = { Text("Pins wirklich entfernen?") },
            text = {
                Text("${selectedIds.size} Pins werden entfernt.")
            },
            confirmButton = {
                TextButton(
                    onClick = {

                        if (selectedTab == 0) {
                            selectedIds.forEach { id ->
                                cityViewModel.updatePersonCoordinates(
                                    id,
                                    null,
                                    null
                                )
                            }
                        } else {
                            selectedIds.forEach { id ->
                                cityViewModel.updatePoiCoordinates(
                                    id,
                                    null,
                                    null
                                )
                            }
                        }

                        confirmDelete = false
                        navController.popBackStack()
                    }
                ) {
                    Text("Löschen")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { confirmDelete = false }
                ) {
                    Text("Abbrechen")
                }
            }
        )
    }
}

/* ---------- Gemeinsame Liste ---------- */

@Composable
private fun <T> PinsList(
    items: List<T>,
    selectedIds: Set<String>,
    onSelectionChange: (Set<String>) -> Unit
) where T : Any {

    if (items.isEmpty()) {
        Text(
            text = "Keine Pins vorhanden",
            modifier = Modifier.padding(16.dp)
        )
        return
    }

    val ids = items.map {
        when (it) {
            is Person -> it.id
            is PointOfInterest -> it.id
            else -> ""
        }
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        TextButton(
            onClick = {
                onSelectionChange(
                    if (selectedIds.size == items.size)
                        emptySet()
                    else
                        ids.toSet()
                )
            }
        ) {
            Text("Alle auswählen")
        }
    }

    items.forEach { item ->

        val id = when (item) {
            is Person -> item.id
            is PointOfInterest -> item.id
            else -> ""
        }

        val name = when (item) {
            is Person -> item.name
            is PointOfInterest -> item.name
            else -> ""
        }

        val checked = selectedIds.contains(id)

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .toggleable(
                    value = checked,
                    onValueChange = {
                        onSelectionChange(
                            if (checked)
                                selectedIds - id
                            else
                                selectedIds + id
                        )
                    }
                )
                .padding(16.dp)
        ) {
            Checkbox(
                checked = checked,
                onCheckedChange = null
            )
            Spacer(Modifier.width(8.dp))
            Text(name)
        }
    }
}