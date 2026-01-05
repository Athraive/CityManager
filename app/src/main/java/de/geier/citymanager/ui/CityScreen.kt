package de.geier.citymanager.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier

// 🔴 WICHTIGE IMPORTS
import de.geier.citymanager.ui.GameMasterCategoryListScreen
import de.geier.citymanager.ui.PlayerCategoryListScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CityScreen(
    isGameMaster: Boolean
) {
    val cityName = "Beispielstadt"

    val tabs = if (isGameMaster) {
        listOf("Karte", "Geschichte", "Vororte", "POI (GM)", "Personen", "Fraktionen")
    } else {
        listOf("Karte", "Geschichte", "Vororte", "POI", "Personen", "Fraktionen")
    }

    var selectedTab by remember { mutableIntStateOf(0) }

    Column(modifier = Modifier.fillMaxSize()) {

        TopAppBar(title = { Text(cityName) })

        TabRow(selectedTabIndex = selectedTab) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    selected = selectedTab == index,
                    onClick = { selectedTab = index },
                    text = { Text(title) }
                )
            }
        }

        when (selectedTab) {
            0 -> Platzhalter("Karte")
            1 -> Platzhalter("Geschichte")
            2 -> Platzhalter("Vororte")
            3 -> {
                if (isGameMaster) {
                    GameMasterCategoryListScreen()
                } else {
                    PlayerCategoryListScreen()
                }
            }
            else -> Platzhalter(tabs[selectedTab])
        }
    }
}

@Composable
private fun Platzhalter(name: String) {
    Text(
        text = "$name\n(Inhalt folgt)",
        style = MaterialTheme.typography.headlineMedium
    )
}
