package de.geier.citymanager.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.*
import de.geier.citymanager.ui.navigation.Route
import de.geier.citymanager.ui.viewmodel.CityViewModel
import de.geier.citymanager.ui.map.MapViewModel

@Composable
fun CityOverviewTab(
    cityViewModel: CityViewModel,
    accessContext: AccessContext,
    mapViewModel: MapViewModel,
    focusPersonId: String? = null,
    focusPoiId: String? = null,
    navController: NavHostController
) {
    val cityId = accessContext.cityId

    /* ---------- AUTO OPEN MAP ---------- */

    LaunchedEffect(Unit) {
        snapshotFlow { focusPersonId to focusPoiId }
            .collect { (personId, poiId) ->

                if (personId != null || poiId != null) {

                    navController.navigate(Route.STADTKARTE) {
                        popUpTo(Route.STADTUEBERSICHT) { inclusive = false }
                        launchSingleTop = true
                    }
                }
            }
    }

    Column(modifier = Modifier.fillMaxSize()) {

        StadtTabs(navController)

        NavHost(
            navController = navController,
            startDestination = Route.STADTUEBERSICHT,
            modifier = Modifier.weight(1f)
        ) {

            composable(Route.STADTUEBERSICHT) {
                CityIntroScreen()
            }

            composable(Route.STADTKARTE) {
                CityMapScreen(
                    cityId = cityId,
                    cityViewModel = cityViewModel,
                    accessContext = accessContext,
                    navController = navController,
                    mapViewModel = mapViewModel,

                    // 🔥 DAS FEHLT
                    focusPersonId = focusPersonId,
                    focusPoiId = focusPoiId
                )
            }

            composable(Route.MANAGE_PINS) {
                ManagePinsScreen(
                    navController = navController,
                    cityViewModel = cityViewModel
                )
            }

            composable(Route.ADD_PIN) { backStackEntry ->

                val x =
                    backStackEntry.arguments?.getString("x")?.toFloat() ?: 0f
                val y =
                    backStackEntry.arguments?.getString("y")?.toFloat() ?: 0f

                AddPinScreen(
                    navController = navController,
                    cityViewModel = cityViewModel,
                    mapX = x,
                    mapY = y
                )
            }

            composable(Route.STADTVIERTEL_LIST) {
                CityDistrictListScreen(
                    cityId = cityId,
                    cityViewModel = cityViewModel,
                    onDistrictSelected = { id ->
                        navController.navigate("stadtviertel/$id")
                    }
                )
            }

            composable(Route.STADTVIERTEL_DETAIL) { backStackEntry ->
                val id =
                    backStackEntry.arguments?.getString("districtId")
                        ?: return@composable

                CityDistrictDetailScreen(
                    districtId = id,
                    cityViewModel = cityViewModel
                )
            }

            composable(Route.STADTGESCHICHTE) {
                StadtgeschichteScreen(
                    cityId = cityId,
                    cityViewModel = cityViewModel
                )
            }
        }
    }
}

@Composable
private fun StadtTabs(
    navController: NavHostController
) {
    val backStackEntry =
        navController.currentBackStackEntryAsState().value

    val currentRoute = backStackEntry?.destination?.route

    val tabs = listOf(
        Route.STADTUEBERSICHT to "Übersicht",
        Route.STADTKARTE to "Stadtkarte",
        Route.STADTVIERTEL_LIST to "Stadtviertel",
        Route.STADTGESCHICHTE to "Stadtgeschichte"
    )

    TabRow(
        selectedTabIndex =
            tabs.indexOfFirst { it.first == currentRoute }.coerceAtLeast(0)
    ) {
        tabs.forEach { (route, title) ->
            Tab(
                selected = currentRoute == route,
                onClick = {
                    if (currentRoute != route) {
                        navController.navigate(route) {
                            launchSingleTop = true
                            popUpTo(Route.STADTUEBERSICHT) {
                                inclusive = false
                            }
                        }
                    }
                },
                text = { Text(title) }
            )
        }
    }
}