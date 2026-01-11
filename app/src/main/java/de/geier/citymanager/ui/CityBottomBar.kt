package de.geier.citymanager.ui

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState

@Composable
fun CityBottomBar(
    navController: NavController,
    isGameMaster: Boolean
) {
    val currentRoute =
        navController.currentBackStackEntryAsState().value?.destination?.route

    fun go(route: String) {
        navController.navigate(route) {
            launchSingleTop = true
            popUpTo("stadt") { saveState = true }
            restoreState = true
        }
    }

    NavigationBar {

        NavigationBarItem(
            selected = currentRoute == "stadt",
            onClick = { go("stadt") },
            icon = {},
            label = { Text("Über die Stadt") }
        )

        NavigationBarItem(
            selected = currentRoute == "personen",
            onClick = { go("personen") },
            icon = {},
            label = { Text("Personen") }
        )

        NavigationBarItem(
            selected = currentRoute == "pois",
            onClick = { go("pois") },
            icon = {},
            label = { Text("POIs") }
        )

        if (isGameMaster) {
            NavigationBarItem(
                selected = currentRoute == "fraktionen",
                onClick = { go("fraktionen") },
                icon = {},
                label = { Text("Fraktionen") }
            )
        }
    }
}
