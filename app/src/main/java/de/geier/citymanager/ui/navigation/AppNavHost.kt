package de.geier.citymanager.ui.navigation

import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import de.geier.citymanager.ui.*

@Composable
fun AppNavHost() {

    val navController = rememberNavController()

    var selectedCityId by rememberSaveable { mutableStateOf<String?>(null) }
    var selectedRole by rememberSaveable { mutableStateOf<Role?>(null) }

    NavHost(
        navController = navController,
        startDestination = Screen.Start.route
    ) {

        /* ---------------- Start ---------------- */

        composable(Screen.Start.route) {
            StartScreen {
                navController.navigate("city_select")
            }
        }

        /* ---------------- City auswählen ---------------- */

        composable("city_select") {

            CitySelectScreen(
                onCitySelected = { cityId ->
                    selectedCityId = cityId
                    navController.navigate("role_select")
                },
                onCreateCity = {
                    navController.navigate("create_city")
                }
            )
        }

        /* ---------------- Stadt erstellen ---------------- */

        composable("create_city") {

            CreateCityScreen(
                onCityCreated = { newCityId ->
                    selectedCityId = newCityId
                    selectedRole = Role.GAME_MASTER
                    navController.navigate(Screen.City.route)
                },
                onCancel = {
                    navController.popBackStack()
                }
            )
        }

        /* ---------------- Rolle wählen ---------------- */

        composable("role_select") {

            val cityId = selectedCityId

            if (cityId == null) {
                navController.popBackStack()
                return@composable
            }

            RoleSelectScreen(
                cityId = cityId,
                onAccessGranted = { role ->
                    selectedRole = role
                    navController.navigate(Screen.City.route)
                }
            )
        }

        /* ---------------- City ---------------- */

        composable(Screen.City.route) {
            CityScreenGate(
                role = selectedRole,
                cityId = selectedCityId
            )
        }


    }
}