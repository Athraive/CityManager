package de.geier.citymanager.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.*
import de.geier.citymanager.ui.map.MapViewModel
import de.geier.citymanager.ui.navigation.Route
import de.geier.citymanager.ui.viewmodel.CityViewModel

@Composable
fun CityOverviewTab(
    cityViewModel: CityViewModel,
    accessContext: AccessContext,
    mapViewModel: MapViewModel,
    focusPersonId: String? = null,
    focusPoiId: String? = null,
    focusTrigger: Int,
    navController: NavHostController,

    onPersonBubbleClick: (String) -> Unit,
    onPoiBubbleClick: (String) -> Unit
) {

    val cityId = accessContext.cityId

    /* ---------- AUTO OPEN MAP ---------- */

    val currentRoute =
        navController.currentBackStackEntryAsState().value?.destination?.route

    LaunchedEffect(focusTrigger) {

        if (
            currentRoute == Route.STADTUEBERSICHT &&
            (focusPersonId != null || focusPoiId != null)
        ) {
            navController.navigate(Route.STADTKARTE) {
                popUpTo(Route.STADTUEBERSICHT) { inclusive = false }
                launchSingleTop = false
            }
        }
    }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {

        StadtTabs(navController)

        NavHost(
            navController = navController,
            startDestination = Route.STADTUEBERSICHT,
            modifier = Modifier.weight(1f)
        ) {

            composable(Route.STADTUEBERSICHT) {

                CityIntroScreen(
                    cityViewModel = cityViewModel,
                    accessContext = accessContext
                )
            }

            composable(Route.STADTKARTE) {

                CityMapScreen(
                    cityId = cityId,
                    cityViewModel = cityViewModel,
                    accessContext = accessContext,
                    navController = navController,
                    mapViewModel = mapViewModel,

                    focusPersonId = focusPersonId,
                    focusPoiId = focusPoiId,

                    onPersonBubbleClick = onPersonBubbleClick,
                    onPoiBubbleClick = onPoiBubbleClick
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
                    accessContext = accessContext,
                    onDistrictSelected = { }
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
                    cityViewModel = cityViewModel,
                    accessContext = accessContext
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

    Box {

        ScrollableTabRow(
            selectedTabIndex =
                tabs.indexOfFirst { it.first == currentRoute }
                    .coerceAtLeast(0),

            edgePadding = 32.dp
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

                    text = {

                        Text(
                            text = title,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            style = MaterialTheme.typography.labelMedium
                        )
                    }
                )
            }
        }

        // 🔹 LINKER PFEIL
        Box(
            modifier = Modifier
                .align(Alignment.CenterStart)
                .padding(start = 4.dp)
                .background(
                    color = MaterialTheme.colorScheme.surface.copy(alpha = 0.75f),
                    shape = CircleShape
                )
                .padding(2.dp)
        ) {

            Icon(
                imageVector = Icons.Default.KeyboardArrowLeft,
                contentDescription = null,
                modifier = Modifier.graphicsLayer(alpha = 0.8f),
                tint = MaterialTheme.colorScheme.onSurface
            )
        }

        // 🔹 RECHTER PFEIL
        Box(
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .padding(end = 4.dp)
                .background(
                    color = MaterialTheme.colorScheme.surface.copy(alpha = 0.75f),
                    shape = CircleShape
                )
                .padding(2.dp)
        ) {

            Icon(
                imageVector = Icons.Default.KeyboardArrowRight,
                contentDescription = null,
                modifier = Modifier.graphicsLayer(alpha = 0.8f),
                tint = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}