package de.geier.citymanager.ui.navigation

import androidx.compose.runtime.*
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

import de.geier.citymanager.ui.CityScreen
import de.geier.citymanager.ui.CitySelectScreen
import de.geier.citymanager.ui.RoleSelectScreen
import de.geier.citymanager.ui.StartScreen

@Composable
fun CityNavHost() {

    val navController = rememberNavController()

    // 🔹 globale Rollen-Info
    val isGameMasterState = remember { mutableStateOf(false) }

    NavHost(
        navController = navController,
        startDestination = "start"
    ) {

        composable("start") {
            StartScreen {
                navController.navigate("role") {
                    popUpTo("start") { inclusive = true }
                }
            }
        }

        composable("role") {
            RoleSelectScreen { isGameMaster ->
                isGameMasterState.value = isGameMaster
                navController.navigate("city") {
                    popUpTo("role") { inclusive = true }
                }
            }
        }

        composable("city") {
            CitySelectScreen(
                onCitySelected = {
                    navController.navigate("cityScreen")
                }
            )
        }

        composable("cityScreen") {
            CityScreen(isGameMaster = isGameMasterState.value)
        }
    }
}
