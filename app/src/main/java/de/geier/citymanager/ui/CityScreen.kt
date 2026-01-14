package de.geier.citymanager.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
                StadtContent(cityViewModel)
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
private fun StadtContent(
    cityViewModel: CityViewModel
) {
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
            StadtTab.STADTVIERTEL -> StadtviertelContent(cityViewModel)
            StadtTab.STADTGESCHICHTE -> StadtgeschichteContent()
        }
    }
}

/* -------------------------------------------------------
 * Stadtkarte (Platzhalter)
 * ----------------------------------------------------- */

@Composable
private fun StadtkarteContent() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .border(2.dp, MaterialTheme.colorScheme.outline)
            .background(MaterialTheme.colorScheme.surfaceVariant),
        contentAlignment = Alignment.Center
    ) {
        Text("Stadtkarte (Platzhalter)")
    }
}

/* -------------------------------------------------------
 * Stadtviertel – Liste + Detail (Phase 4)
 * ----------------------------------------------------- */

@Composable
private fun StadtviertelContent(
    cityViewModel: CityViewModel
) {
    val localNavController = rememberNavController()
    val cityId = "default" // 🔹 später ersetzen

    NavHost(
        navController = localNavController,
        startDestination = "list"
    ) {

        composable("list") {
            CityDistrictListScreen(
                cityId = cityId,
                cityViewModel = cityViewModel,
                onDistrictSelected = { districtId ->
                    localNavController.navigate("detail/$districtId")
                }
            )
        }

        composable("detail/{districtId}") { backStackEntry ->
            val districtId =
                backStackEntry.arguments?.getString("districtId")
                    ?: return@composable

            CityDistrictDetailScreen(
                districtId = districtId,
                cityViewModel = cityViewModel
            )
        }
    }
}

/* -------------------------------------------------------
 * Stadtgeschichte (Platzhalter)
 * ----------------------------------------------------- */

@Composable
private fun StadtgeschichteContent() {
    Column(modifier = Modifier.padding(16.dp)) {
        Text("Stadtgeschichte", style = MaterialTheme.typography.titleLarge)
        Spacer(Modifier.height(8.dp))
        Text("Allgemeine Geschichte der Stadt.")
    }
}

/* -------------------------------------------------------
 * Platzhalter
 * ----------------------------------------------------- */

@Composable
private fun ScreenPlaceholder(title: String) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text("$title – Inhalt (Platzhalter)")
    }
}
