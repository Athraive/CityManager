package de.geier.citymanager.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import de.geier.citymanager.ui.viewmodel.CityViewModel
import de.geier.citymanager.ui.viewmodel.PersonViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CityScreen(
    cityViewModel: CityViewModel,
    isGameMaster: Boolean = false
) {
    val cityName = "Beispielstadt"
    var selectedTab by remember { mutableIntStateOf(0) }

    val context = LocalContext.current

    val poiCategoryViewModel: PoiCategoryViewModel = viewModel(
        factory = PoiCategoryViewModelFactory(context)
    )

    val personViewModel: PersonViewModel = viewModel(
        factory = PersonViewModelFactory(context)
    )

    val allPois by cityViewModel.allPois.collectAsState()
    val categories by poiCategoryViewModel.categories.collectAsState()
    val factions by cityViewModel.factions.collectAsState()
    val persons by personViewModel.persons.collectAsState()

    Column(modifier = Modifier.fillMaxSize()) {

        TopAppBar(
            title = { Text(cityName) }
        )

        TabRow(selectedTabIndex = selectedTab) {
            listOf("Karte", "Geschichte", "POI", "Personen", "Fraktionen")
                .forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTab == index,
                        onClick = { selectedTab = index },
                        text = { Text(title) }
                    )
                }
        }

        when (selectedTab) {
            0 -> PlatzhalterTab("Karte")
            1 -> PlatzhalterTab("Geschichte")

            /* ---------------- POIs ---------------- */

            2 -> {
                if (isGameMaster) {
                    GameMasterCategoryListScreen(
                        categoryViewModel = poiCategoryViewModel,
                        cityViewModel = cityViewModel,
                        allPois = allPois
                    )
                } else {
                    PlayerCategoryListScreen(
                        cityViewModel = cityViewModel,
                        categoryViewModel = poiCategoryViewModel,
                        persons = persons,
                        personViewModel = personViewModel
                    )

                }
            }

            /* ---------------- PERSONEN ---------------- */

            3 -> PersonenTab(
                viewModel = personViewModel,
                factions = factions,
                pois = allPois,
                categories = categories,
                isGameMaster = isGameMaster
            )

            /* ---------------- FRAKTIONEN ---------------- */

            4 -> FraktionenTab(
                cityViewModel = cityViewModel,
                isGameMaster = isGameMaster
            )
        }
    }
}

@Composable
private fun PlatzhalterTab(name: String) {
    Text(
        text = "$name\n(Inhalt folgt)",
        style = MaterialTheme.typography.headlineMedium,
        modifier = Modifier.padding(24.dp)
    )
}
