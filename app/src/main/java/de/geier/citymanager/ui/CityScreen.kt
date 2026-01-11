package de.geier.citymanager.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.*
import de.geier.citymanager.ui.viewmodel.CityViewModel

@Composable
fun CityScreen(
    cityViewModel: CityViewModel,
    isGameMaster: Boolean
) {
    val navController = rememberNavController()

    Scaffold(
        bottomBar = {
            CityBottomBar(
                navController = navController,
                isGameMaster = isGameMaster
            )
        }
    ) { padding ->

        NavHost(
            navController = navController,
            startDestination = "stadt",
            modifier = Modifier.padding(padding)
        ) {

            composable("stadt") {
                StadtContent()
            }

            composable("personen") {
                ScreenPlaceholder("Personen")
            }

            composable("pois") {
                ScreenPlaceholder("POIs")
            }

            composable("fraktionen") {
                ScreenPlaceholder("Fraktionen")
            }
        }
    }
}

/* -------------------------------------------------------
 * Über die Stadt
 * ----------------------------------------------------- */

private enum class StadtTab(val title: String) {
    STADTKARTE("Stadtkarte"),
    STADTVIERTEL("Stadtviertel"),
    STADTGESCHICHTE("Stadtgeschichte")
}

@Composable
private fun StadtContent() {
    var selectedTab by remember { mutableStateOf(StadtTab.STADTKARTE) }

    Column(modifier = Modifier.fillMaxSize()) {

        TabRow(selectedTabIndex = selectedTab.ordinal) {
            StadtTab.values().forEach { tab ->
                Tab(
                    selected = selectedTab == tab,
                    onClick = { selectedTab = tab },
                    text = { Text(tab.title) }
                )
            }
        }

        when (selectedTab) {
            StadtTab.STADTKARTE -> StadtkarteContent()
            StadtTab.STADTVIERTEL -> StadtviertelContent()
            StadtTab.STADTGESCHICHTE -> StadtgeschichteContent()
        }
    }
}

/* -------------------------------------------------------
 * Stadtkarte (visueller Platzhalter)
 * ----------------------------------------------------- */

@Composable
private fun StadtkarteContent() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .border(
                width = 2.dp,
                color = MaterialTheme.colorScheme.outline,
                shape = MaterialTheme.shapes.medium
            )
            .background(
                color = MaterialTheme.colorScheme.surfaceVariant,
                shape = MaterialTheme.shapes.medium
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {

            Text(
                text = "Stadtkarte",
                style = MaterialTheme.typography.titleLarge
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Hier wird später die Stadtkarte angezeigt.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

/* -------------------------------------------------------
 * Weitere Inhalte
 * ----------------------------------------------------- */

@Composable
private fun StadtviertelContent() {
    Column(modifier = Modifier.padding(16.dp)) {
        Text("Stadtviertel", style = MaterialTheme.typography.titleLarge)
        Spacer(Modifier.height(8.dp))
        Text("Allgemeine Beschreibungen der Stadtviertel.")
    }
}

@Composable
private fun StadtgeschichteContent() {
    Column(modifier = Modifier.padding(16.dp)) {
        Text("Stadtgeschichte", style = MaterialTheme.typography.titleLarge)
        Spacer(Modifier.height(8.dp))
        Text("Allgemeine Geschichte der Stadt.")
    }
}

@Composable
private fun ScreenPlaceholder(title: String) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "$title – Inhalt (Platzhalter)",
            style = MaterialTheme.typography.titleLarge
        )
    }
}
