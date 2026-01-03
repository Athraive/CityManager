package de.geier.citymanager.ui.navigation

import androidx.compose.runtime.*
import androidx.navigation.NavType
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import de.geier.citymanager.ui.*

@Composable
fun CityNavHost(
    navController: NavHostController
) {
    // ─────────────────────────────
    // Kategorien (zentraler State)
    // ─────────────────────────────
    var categories by remember {
        mutableStateOf(
            listOf(
                PoiCategory("1", "Stadtviertel", "🏘️", null),
                PoiCategory("2", "Gasthäuser", "🍺", null)
            )
        )
    }

    NavHost(
        navController = navController,
        startDestination = Screen.Start.route
    ) {

        // ───── Start ─────
        composable(Screen.Start.route) {
            StartScreen(
                cityName = "Eldoria",
                onStartClicked = {
                    navController.navigate(Screen.RoleSelect.route)
                }
            )
        }

        // ───── Rollenwahl ─────
        composable(Screen.RoleSelect.route) {
            RoleSelectScreen(
                onPlayerClick = { /* später */ },
                onGameMasterClick = {
                    navController.navigate(Screen.GameMasterDashboard.route)
                }
            )
        }

        // ───── Spielleiter Dashboard ─────
        composable(Screen.GameMasterDashboard.route) {
            GameMasterDashboardScreen(
                onBack = { navController.popBackStack() },
                onCityDescription = { /* später */ },
                onGroups = { /* später */ },
                onLocations = {
                    // ➜ Kategorien → POIs
                    navController.navigate(Screen.CategoryList.route)
                },
                onShops = {
                    // ebenfalls Kategorien (keine getrennte Route mehr!)
                    navController.navigate(Screen.CategoryList.route)
                },
                onPeople = { /* später */ },
                onNotes = { /* später */ },
                onVisibility = { /* später */ }
            )
        }

        // ───── Kategorienliste ─────
        composable(Screen.CategoryList.route) {
            CategoryListScreen(
                categories = categories,
                onCategoryClick = { category ->
                    // STANDARD: erst mal Orte anzeigen
                    navController.navigate(
                        Screen.PoiList.createRoute(
                            categoryId = category.id,
                            type = PoiType.LOCATION
                        )
                    )
                },
                onAddCategory = {
                    navController.navigate(
                        Screen.CategoryEdit.createRoute(null)
                    )
                },
                onDeleteCategory = { category ->
                    categories = categories.filterNot { it.id == category.id }
                },
                onBack = {
                    navController.popBackStack()
                }
            )
        }

        // ───── Kategorie bearbeiten / neu ─────
        composable(
            route = Screen.CategoryEdit.route,
            arguments = listOf(
                navArgument("categoryId") {
                    type = NavType.StringType
                    nullable = true
                    defaultValue = null
                }
            )
        ) { entry ->

            val categoryId = entry.arguments?.getString("categoryId")
            val existing = categories.find { it.id == categoryId }

            CategoryEditScreen(
                category = existing,
                onSave = { updated ->
                    categories =
                        categories.filterNot { it.id == updated.id } + updated
                    navController.popBackStack()
                },
                onCancel = {
                    navController.popBackStack()
                }
            )
        }

        // ───── POIs einer Kategorie ─────
        composable(
            route = Screen.PoiList.route,
            arguments = listOf(
                navArgument("categoryId") { type = NavType.StringType },
                navArgument("type") { type = NavType.StringType }
            )
        ) { entry ->

            val categoryId =
                entry.arguments!!.getString("categoryId")!!

            val type =
                PoiType.valueOf(
                    entry.arguments!!.getString("type")!!
                )

            PointOfInterestScreen(
                poiType = type,
                categoryId = categoryId,
                isGameMaster = true,   // 👈 SL
                onBack = { navController.popBackStack() }
            )

        }
    }
}
