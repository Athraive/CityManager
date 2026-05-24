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
fun AddPinScreen(
    navController: NavController,
    cityViewModel: CityViewModel,
    mapX: Float,
    mapY: Float
) {

    val persons by cityViewModel.allPersons.collectAsState()
    val pois by cityViewModel.allPois.collectAsState()

    var selectedTab by remember { mutableStateOf(0) }

    // 🔹 Nur Einträge ohne bestehenden Pin anzeigen
    val personsWithoutPin =
        persons.filter { it.mapX == null || it.mapY == null }

    val poisWithoutPin =
        pois.filter { it.mapX == null || it.mapY == null }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Pin hinzufügen",
                        style = MaterialTheme.typography.bodyLarge
                    )
                },
                actions = {
                    TextButton(
                        onClick = { navController.popBackStack() }
                    ) {
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

            if (selectedTab == 0) {

                if (personsWithoutPin.isEmpty()) {
                    Text(
                        text = "Alle Personen haben bereits einen Pin.",
                        style = MaterialTheme.typography.bodyMedium
                    )
                } else {

                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState())
                    ) {

                        personsWithoutPin.forEach { person ->

                            Button(
                                onClick = {
                                    cityViewModel.updatePersonCoordinates(
                                        person.id,
                                        mapX,
                                        mapY
                                    )
                                    navController.popBackStack()
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(bottom = 8.dp)
                            ) {
                                Text(
                                    text = person.name,
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            }
                        }
                    }
                }

            } else {

                if (poisWithoutPin.isEmpty()) {
                    Text(
                        text = "Alle POIs haben bereits einen Pin.",
                        style = MaterialTheme.typography.bodyMedium
                    )
                } else {

                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState())
                    ) {

                        poisWithoutPin.forEach { poi ->

                            Button(
                                onClick = {
                                    cityViewModel.updatePoiCoordinates(
                                        poi.id,
                                        mapX,
                                        mapY
                                    )
                                    navController.popBackStack()
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(bottom = 8.dp)
                            ) {
                                Text(
                                    text = poi.name,
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}