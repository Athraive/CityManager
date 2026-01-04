package de.geier.citymanager.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import de.geier.citymanager.ui.*
import de.geier.citymanager.ui.viewmodel.CityViewModel

@Composable
fun CityNavHost(
    navController: NavHostController
) {
    NavHost(
        navController = navController,
        startDestination = Screen.CategoryList.route
    ) {

        // ─────────────────────────────
        // SL – Kategorien
        // ─────────────────────────────
        composable(Screen.CategoryList.route) {

            val cityViewModel: CityViewModel = viewModel()
            val categories by cityViewModel.categories.collectAsState()

            LaunchedEffect(Unit) {
                cityViewModel.loadCategories()
            }

            CategoryListScreen(
                categories = categories,
                onCategoryClick = { category ->
                    navController.navigate(
                        Screen.Pois.createRoute(category.id)
                    )
                },
                onAddCategory = {
                    navController.navigate(
                        Screen.CategoryEdit.createRoute(null)
                    )
                },
                onDeleteCategory = { category ->
                    cityViewModel.deleteCategory(category)
                },
                onBack = {
                    navController.popBackStack()
                }
            )
        }

        // ─────────────────────────────
        // SL – Kategorie bearbeiten / neu
        // ─────────────────────────────
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

            val cityViewModel: CityViewModel = viewModel()
            val categories by cityViewModel.categories.collectAsState()

            val categoryId = entry.arguments?.getString("categoryId")
            val existingCategory =
                categories.find { it.id == categoryId }

            CategoryEditScreen(
                category = existingCategory,
                onSave = { category ->
                    cityViewModel.saveCategory(category)
                    navController.popBackStack()
                },
                onCancel = {
                    navController.popBackStack()
                }
            )
        }

        // ─────────────────────────────
        // SL – POIs (Tabs: Orte / Geschäfte)
        // ─────────────────────────────
        composable(
            route = Screen.Pois.route,
            arguments = listOf(
                navArgument("categoryId") {
                    type = NavType.StringType
                }
            )
        ) { entry ->

            val categoryId =
                entry.arguments?.getString("categoryId") ?: return@composable

            PointOfInterestScreen(
                categoryId = categoryId,
                onBack = {
                    navController.popBackStack()
                }
            )
        }

        // ─────────────────────────────
        // Spieler – Kategorien (read-only)
        // ─────────────────────────────
        composable(Screen.PlayerCategories.route) {

            PlayerCategoryListScreen(
                onCategoryClick = { category ->
                    navController.navigate(
                        Screen.PlayerPois.createRoute(category.id)
                    )
                },
                onBack = {
                    navController.popBackStack()
                }
            )
        }

        // ─────────────────────────────
        // Spieler – POIs (read-only, Tabs)
        // ─────────────────────────────
        composable(
            route = Screen.PlayerPois.route,
            arguments = listOf(
                navArgument("categoryId") {
                    type = NavType.StringType
                }
            )
        ) { entry ->

            val categoryId =
                entry.arguments?.getString("categoryId") ?: return@composable

            PlayerPoiScreen(
                navController = navController,
                categoryId = categoryId,
                onBack = {
                    navController.popBackStack()
                }
            )
        }

        // ─────────────────────────────
        // Spieler – POI Detail
        // ─────────────────────────────
        composable(
            route = Screen.PlayerPoiDetail.route,
            arguments = listOf(
                navArgument("poiId") {
                    type = NavType.StringType
                }
            )
        ) { entry ->

            val poiId =
                entry.arguments?.getString("poiId") ?: return@composable

            val cityViewModel: CityViewModel = viewModel()
            val pois by cityViewModel.pois.collectAsState()

            val poi =
                pois.firstOrNull { it.id == poiId } ?: return@composable

            PlayerPoiDetailScreen(
                poi = poi,
                onBack = {
                    navController.popBackStack()
                }
            )
        }
    }
}
