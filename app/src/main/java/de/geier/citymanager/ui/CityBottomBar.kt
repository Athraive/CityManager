package de.geier.citymanager.ui

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import de.geier.citymanager.ui.navigation.Route

@Composable
fun CityBottomBar(
    navController: NavController,
    isGameMaster: Boolean
) {
    val currentRoute =
        navController.currentBackStackEntryAsState().value?.destination?.route

    fun go(route: String) {
        if (currentRoute == route) return
        navController.navigate(route) {
            launchSingleTop = true
            popUpTo(route) { inclusive = false }
        }
    }

    NavigationBar {

        NavigationBarItem(
            selected = currentRoute == Route.STADTKARTE,
            onClick = { go(Route.STADTKARTE) },
            icon = {},
            label = { Text("Über die Stadt") }
        )

        NavigationBarItem(
            selected = currentRoute == Route.PERSONEN,
            onClick = { go(Route.PERSONEN) },
            icon = {},
            label = { Text("Personen") }
        )

        NavigationBarItem(
            selected = currentRoute == Route.POIS,
            onClick = { go(Route.POIS) },
            icon = {},
            label = { Text("POIs") }
        )

        if (isGameMaster) {
            NavigationBarItem(
                selected = currentRoute == Route.FRAKTIONEN,
                onClick = { go(Route.FRAKTIONEN) },
                icon = {},
                label = { Text("Fraktionen") }
            )
        }
    }
}
