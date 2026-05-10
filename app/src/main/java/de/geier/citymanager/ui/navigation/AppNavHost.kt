package de.geier.citymanager.ui.navigation

import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import de.geier.citymanager.ui.*
import de.geier.citymanager.ui.viewmodel.CitySelectViewModel
import de.geier.citymanager.ui.viewmodel.CitySelectViewModelFactory

@Composable
fun AppNavHost() {

    val navController = rememberNavController()

    var selectedCityId by rememberSaveable {
        mutableStateOf<String?>(null)
    }

    var selectedRole by rememberSaveable {
        mutableStateOf<Role?>(null)
    }

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
                },

                onEditCity = { cityId ->

                    selectedCityId = cityId

                    navController.navigate("edit_city")
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

        /* ---------------- Stadt bearbeiten ---------------- */

        composable("edit_city") {

            val cityId = selectedCityId

            if (cityId == null) {

                navController.popBackStack()

                return@composable
            }

            val context =
                androidx.compose.ui.platform.LocalContext.current

            val viewModel: CitySelectViewModel = viewModel(
                factory = CitySelectViewModelFactory(context)
            )

            val cities by viewModel.cities.collectAsState()

            val city =
                cities.firstOrNull { it.id == cityId }

            if (city != null) {

                CreateCityScreen(

                    existingCity = city,

                    onCityCreated = {},

                    onCityUpdated = {

                        navController.popBackStack()
                    },

                    onCancel = {

                        navController.popBackStack()
                    }
                )
            }
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