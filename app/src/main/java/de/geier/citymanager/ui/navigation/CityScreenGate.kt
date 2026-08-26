package de.geier.citymanager.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import de.geier.citymanager.ui.AccessContext
import de.geier.citymanager.ui.CityScreen
import de.geier.citymanager.ui.ManagePinsScreen
import de.geier.citymanager.ui.Role
import de.geier.citymanager.ui.viewmodel.CityViewModel
import de.geier.citymanager.ui.viewmodel.CityViewModelFactory

@Composable
fun CityScreenGate(
    role: Role?,
    cityId: String?,
    managePinsMode: Boolean = false,
    navController: NavController
) {
    if (role == null || cityId == null) return

    val context = LocalContext.current

    val accessContext = AccessContext(
        role = role,
        cityId = cityId
    )
    println(
        "CityScreenGate: role=$role accessRole=${accessContext.role} cityId=$cityId"
    )

    val cityViewModel: CityViewModel = viewModel(
        factory = CityViewModelFactory(
            context = context,
            accessContext = accessContext
        )
    )

    if (managePinsMode) {

        ManagePinsScreen(
            navController = navController,
            cityViewModel = cityViewModel
        )

    } else {

        CityScreen(
            cityViewModel = cityViewModel,
            accessContext = accessContext,
            navController = navController
        )
    }
}