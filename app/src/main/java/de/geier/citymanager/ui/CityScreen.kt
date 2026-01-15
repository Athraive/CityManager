package de.geier.citymanager.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import de.geier.citymanager.ui.navigation.Route
import de.geier.citymanager.ui.viewmodel.CityViewModel
import de.geier.citymanager.ui.viewmodel.PersonViewModel
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue


@Composable
fun CityScreen(
    cityViewModel: CityViewModel,
    isGameMaster: Boolean
) {
    val navController = rememberNavController()
    val context = LocalContext.current
    val cityId = "default"

    /* ---------------- ViewModels ---------------- */

    val personViewModel: PersonViewModel = viewModel(
        factory = PersonViewModelFactory(context)
    )

    Scaffold(
        bottomBar = {
            CityBottomBar(
                navController = navController,
                isGameMaster = isGameMaster
            )
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {

            /* ---------------- Stadt-Tabs ---------------- */

            StadtTabs(navController)

            /* ---------------- Content ---------------- */

            NavHost(
                navController = navController,
                startDestination = Route.STADTKARTE,
                modifier = Modifier.weight(1f)
            ) {

                composable(Route.STADTKARTE) {
                    // Stadtkarte folgt später
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

                /* ---------------- Fach-Tabs ---------------- */

                composable(Route.PERSONEN) {
                    PersonenTab(
                        viewModel = personViewModel,
                        pois = emptyList(),
                        categories = emptyList(),
                        isGameMaster = isGameMaster
                    )

                }

                composable(Route.POIS) {

                    val categoryViewModel: PoiCategoryViewModel = viewModel(
                        factory = PoiCategoryViewModelFactory(context)
                    )

                    val allPois by cityViewModel.allPois.collectAsState()

                    if (isGameMaster) {

                        GameMasterCategoryListScreen(
                            categoryViewModel = categoryViewModel,
                            cityViewModel = cityViewModel,
                            allPois = allPois
                        )

                    } else {

                        PlayerCategoryListScreen(
                            cityViewModel = cityViewModel,
                            categoryViewModel = categoryViewModel,
                            persons = emptyList(),          // unverändert wie vorher
                            personViewModel = personViewModel
                        )
                    }
                }




                composable(Route.FRAKTIONEN) {
                    FraktionenTab(
                        cityViewModel = cityViewModel,
                        isGameMaster = isGameMaster
                    )
                }
            }
        }
    }
}

/* -------------------------------------------------------
 * Stadt-Tabs
 * ----------------------------------------------------- */

@Composable
private fun StadtTabs(
    navController: NavController
) {
    val backStackEntry =
        navController.currentBackStackEntryAsState().value

    val currentRoute = backStackEntry?.destination?.route

    val tabs = listOf(
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
                        }
                    }
                },
                text = { Text(title) }
            )
        }
    }
}
