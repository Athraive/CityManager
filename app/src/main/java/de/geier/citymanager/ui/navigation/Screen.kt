package de.geier.citymanager.ui.navigation

sealed class Screen(val route: String) {

    // ─────────────────────────────
    // Spielleiter
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

    object Pois : Screen("pois/{categoryId}") {
        fun createRoute(categoryId: String): String =
            "pois/$categoryId"
    }

    // ─────────────────────────────
    // Spieler
    // ─────────────────────────────
    object PlayerCategories : Screen("player_categories")

    object PlayerPois : Screen("player_pois/{categoryId}") {
        fun createRoute(categoryId: String): String =
            "player_pois/$categoryId"
    }

    object PlayerPoiDetail : Screen("player_poi_detail/{poiId}") {
        fun createRoute(poiId: String): String =
            "player_poi_detail/$poiId"
    }
}
