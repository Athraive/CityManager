package de.geier.citymanager.ui.navigation

import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import de.geier.citymanager.ui.CitySelectScreen
import de.geier.citymanager.ui.Role
import de.geier.citymanager.ui.RoleSelectScreen
import de.geier.citymanager.ui.StartScreen

@Composable
fun AppNavHost() {

    val navController = rememberNavController()

    var selectedRole by rememberSaveable { mutableStateOf<Role?>(null) }
    var selectedCityId by rememberSaveable { mutableStateOf<Long?>(null) }

    NavHost(
        navController = navController,
        startDestination = Screen.Start.route
    ) {

        composable(Screen.Start.route) {
            StartScreen {
                navController.navigate(Screen.RoleSelect.route)
            }
        }

        composable(Screen.RoleSelect.route) {
            RoleSelectScreen { role ->
                selectedRole = role
                navController.navigate("city_select")
            }
        }

        composable("city_select") {
            CitySelectScreen { cityId ->
                selectedCityId = cityId
                navController.navigate(Screen.City.route)
            }
        }

        // ✅ CITY existiert IMMER
        composable(Screen.City.route) {
            CityScreenGate(
                role = selectedRole,
                cityId = selectedCityId
            )
        }
    }
}
