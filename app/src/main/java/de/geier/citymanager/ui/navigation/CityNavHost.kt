package de.geier.citymanager.ui.navigation

import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import de.geier.citymanager.ui.AccessContext
import de.geier.citymanager.ui.CityScreen
import de.geier.citymanager.ui.Role
import de.geier.citymanager.ui.viewmodel.CityViewModel
import de.geier.citymanager.ui.viewmodel.CityViewModelFactory

fun NavGraphBuilder.cityNavGraph(
    navController: NavController,
    cityId: String,
    role: Role
) {
    composable(Screen.City.route) {

        val context = LocalContext.current

        val accessContext = remember {
            AccessContext(
                role = role,
                cityId = cityId
            )
        }

        val cityViewModel: CityViewModel = viewModel(
            factory = CityViewModelFactory(
                context = context,
                accessContext = accessContext
            )
        )

        CityScreen(
            cityViewModel = cityViewModel,
            accessContext = accessContext
        )
    }
}
