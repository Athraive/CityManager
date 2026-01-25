package de.geier.citymanager.ui.navigation

sealed class Screen(val route: String) {

    object Start : Screen("start")
    object RoleSelect : Screen("role_select")

    object City : Screen("city")

    object PersonDetail : Screen("person/{id}") {
        fun createRoute(id: String) = "person/$id"
    }

    object PoiDetail : Screen("poi/{id}") {
        fun createRoute(id: String) = "poi/$id"
    }

    object AssignPoisToPerson : Screen("person/{id}/assign_pois") {
        fun createRoute(id: String) = "person/$id/assign_pois"
    }
}
