package de.geier.citymanager.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import de.geier.citymanager.ui.viewmodel.CityViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CityScreen(
    cityViewModel: CityViewModel,
    isGameMaster: Boolean = false
) {
    val cityName = "Beispielstadt"
    var selectedTab by remember { mutableIntStateOf(0) }

    val poiCategoryViewModel: PoiCategoryViewModel = viewModel(
        factory = PoiCategoryViewModelFactory(LocalContext.current)
    )

    // 🔹 NEU: PersonViewModel
    val personViewModel: PersonViewModel = viewModel(
        factory = PersonViewModelFactory(LocalContext.current)
    )

    val allPois by cityViewModel.allPois.collectAsState()

    Column(modifier = Modifier.fillMaxSize()) {

        TopAppBar(title = { Text(cityName) })

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
                        allPois = allPois
                    )
                }
            }

            // 🔹 HIER DER ENTSCHEIDENDE FIX
            3 -> PersonenTab(
                viewModel = personViewModel
            )

            4 -> FraktionenTab(cityViewModel, isGameMaster)
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
