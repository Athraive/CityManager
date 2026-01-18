package de.geier.citymanager.ui.navigation

/**
 * Zentrale Screen-Definition für echte NavHost-Wechsel.
 *
 * Hinweis:
 * - Fach-Tabs (Personen, POIs, Fraktionen, Stadt) sind bewusst
 *   NICHT hier modelliert.
 * - Diese werden state-basiert innerhalb von CityScreen gesteuert.
 */
sealed class Screen(val route: String) {

    // ─────────────────────────────
    // App-Start & Kontextwahl
    // ─────────────────────────────

    object Start : Screen("start")

    object RoleSelect : Screen("role")
}
