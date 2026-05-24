package de.geier.citymanager.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
    var confirmDelete by remember { mutableStateOf(false) }

    val personsWithPins = persons.filter { it.mapX != null && it.mapY != null }
    val poisWithPins = pois.filter { it.mapX != null && it.mapY != null }

    val selectedPersons = remember { mutableStateListOf<String>() }
    val selectedPois = remember { mutableStateListOf<String>() }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Pins verwalten",
                        style = MaterialTheme.typography.bodyLarge
                    )
                },
                actions = {
                    TextButton(onClick = { navController.popBackStack() }) {
                        Text(
                            text = "Abbrechen",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            )
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxSize()
        ) {

            TabRow(selectedTabIndex = selectedTab) {

                Tab(
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 },
                    text = {
                        Text(
                            text = "Personen",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                )

                Tab(
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 },
                    text = {
                        Text(
                            text = "POIs",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                )
            }

            Spacer(Modifier.height(16.dp))

            val list = if (selectedTab == 0)
                personsWithPins
            else
                poisWithPins

            if (list.isEmpty()) {
                Text(
                    text = "Keine Pins vorhanden.",
                    style = MaterialTheme.typography.bodyMedium
                )
            } else {

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .verticalScroll(rememberScrollState())
                ) {

                    if (selectedTab == 0) {
                        personsWithPins.forEach { person ->

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {

                                Text(
                                    text = person.name,
                                    style = MaterialTheme.typography.bodyMedium
                                )

                                Checkbox(
                                    checked = selectedPersons.contains(person.id),
                                    onCheckedChange = { checked ->
                                        if (checked)
                                            selectedPersons.add(person.id)
                                        else
                                            selectedPersons.remove(person.id)
                                    }
                                )
                            }

                            Spacer(Modifier.height(8.dp))
                        }
                    } else {

                        poisWithPins.forEach { poi ->

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {

                                Text(
                                    text = poi.name,
                                    style = MaterialTheme.typography.bodyMedium
                                )

                                Checkbox(
                                    checked = selectedPois.contains(poi.id),
                                    onCheckedChange = { checked ->
                                        if (checked)
                                            selectedPois.add(poi.id)
                                        else
                                            selectedPois.remove(poi.id)
                                    }
                                )
                            }

                            Spacer(Modifier.height(8.dp))
                        }
                    }
                }

                Spacer(Modifier.height(16.dp))

                Button(
                    onClick = { confirmDelete = true },
                    enabled = selectedPersons.isNotEmpty() || selectedPois.isNotEmpty(),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Ausgewählte Pins löschen",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }
    }

    if (confirmDelete) {
        AlertDialog(
            onDismissRequest = { confirmDelete = false },
            title = {
                Text(
                    text = "Wirklich löschen?",
                    style = MaterialTheme.typography.bodyLarge
                )
            },
            text = {
                Text(
                    text = "Die ausgewählten Pins werden entfernt.",
                    style = MaterialTheme.typography.bodyMedium
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {

                        selectedPersons.forEach {
                            cityViewModel.updatePersonCoordinates(it, null, null)
                        }

                        selectedPois.forEach {
                            cityViewModel.updatePoiCoordinates(it, null, null)
                        }

                        confirmDelete = false
                        navController.popBackStack()
                    }
                ) {
                    Text(
                        text = "Löschen",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { confirmDelete = false }
                ) {
                    Text(
                        text = "Abbrechen",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        )
    }
}