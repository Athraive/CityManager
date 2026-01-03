package de.geier.citymanager.ui.navigation

import de.geier.citymanager.ui.PoiType

sealed class Screen(val route: String) {

    // ─────────────────────────────
    // Start & Rollen
    // ─────────────────────────────
    object Start : Screen("start")
    object RoleSelect : Screen("role_select")

    // ─────────────────────────────
    // Spielleiter
    // ─────────────────────────────
    object GameMasterDashboard : Screen("gm_dashboard")

    // ─────────────────────────────
    // Kategorien
    // ─────────────────────────────
    object CategoryList : Screen("category_list")

    object CategoryEdit : Screen("category_edit?categoryId={categoryId}") {
        fun createRoute(categoryId: String?): String =
            if (categoryId == null) {
                "category_edit"
            } else {
                "category_edit?categoryId=$categoryId"
            }
    }

    // ─────────────────────────────
    // POIs (an Kategorie gebunden)
    // ─────────────────────────────
    object PoiList : Screen("poi_list/{categoryId}/{type}") {
        fun createRoute(
            categoryId: String,
            type: PoiType
        ): String =
            "poi_list/$categoryId/${type.name}"
    }
}
