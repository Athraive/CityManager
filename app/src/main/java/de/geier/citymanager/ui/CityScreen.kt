package de.geier.citymanager.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import de.geier.citymanager.ui.viewmodel.CityViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CityScreen(
    cityViewModel: CityViewModel,
    isGameMaster: Boolean = false
) {
    val cityName = "Beispielstadt"
    var selectedTab by remember { mutableIntStateOf(0) }

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
            0 -> KarteTab()
            1 -> PlatzhalterTab("Geschichte")
            2 -> {
                Text(
                    text = "POIs\n(Inhalt folgt)",
                    style = MaterialTheme.typography.headlineMedium,
                    modifier = Modifier.padding(24.dp)
                )
            }
            3 -> PlatzhalterTab("Personen")
            4 -> FraktionenTab(
                cityViewModel = cityViewModel,
                isGameMaster = isGameMaster
            )
        }
    }
}

/* =========================
   Platzhalter-Tabs
   ========================= */

@Composable
private fun KarteTab() {
    Text(
        text = "Karte\n(hier kommt später die Stadtkarte)",
        style = MaterialTheme.typography.headlineMedium,
        modifier = Modifier.padding(24.dp)
    )
}

@Composable
private fun PlatzhalterTab(name: String) {
    Text(
        text = "$name\n(Inhalt folgt)",
        style = MaterialTheme.typography.headlineMedium,
        modifier = Modifier.padding(24.dp)
    )
}
