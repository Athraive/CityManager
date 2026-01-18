package de.geier.citymanager.ui.navigation

import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import de.geier.citymanager.ui.CityScreen
import de.geier.citymanager.ui.CitySelectScreen
import de.geier.citymanager.ui.RoleSelectScreen
import de.geier.citymanager.ui.StartScreen
import de.geier.citymanager.ui.viewmodel.CityViewModel
import de.geier.citymanager.ui.viewmodel.CityViewModelFactory

@Composable
fun CityNavHost() {

    val navController = rememberNavController()

    // 🔹 globale Rollen-Info (konfigurationssicher)
    val isGameMasterState = rememberSaveable { mutableStateOf(false) }

    // 🔹 CityViewModel zentral erzeugen
    val context = LocalContext.current
    val cityViewModel: CityViewModel = viewModel(
        factory = CityViewModelFactory(context)
    )

    NavHost(
        navController = navController,
        startDestination = Screen.Start.route
    ) {

        composable(Screen.Start.route) {
            StartScreen {
                navController.navigate(Screen.RoleSelect.route) {
                    popUpTo(Screen.Start.route) { inclusive = true }
                }
            }
        }

        composable(Screen.RoleSelect.route) {
            RoleSelectScreen { isGameMaster ->
                isGameMasterState.value = isGameMaster
                navController.navigate("city") {
                    popUpTo(Screen.RoleSelect.route) { inclusive = true }
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
            CityScreen(
                cityViewModel = cityViewModel,
                isGameMaster = isGameMasterState.value
            )
        }
    }
}
