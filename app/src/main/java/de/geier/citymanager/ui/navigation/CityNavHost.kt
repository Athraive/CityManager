package de.geier.citymanager.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import de.geier.citymanager.ui.CityScreen
import de.geier.citymanager.ui.viewmodel.CityViewModel
import de.geier.citymanager.ui.viewmodel.CityViewModelFactory

@Composable
fun CityNavHost(
    navController: NavHostController,
    isGameMaster: Boolean
) {
    val context = LocalContext.current

    val cityViewModel: CityViewModel = viewModel(
        factory = CityViewModelFactory(context)
    )

    NavHost(
        navController = navController,
        startDestination = Screen.City.route
    ) {
        composable(Screen.City.route) {
            CityScreen(
                cityViewModel = cityViewModel,
                isGameMaster = isGameMaster
            )
        }
    }
}
