package de.geier.citymanager.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import de.geier.citymanager.ui.viewmodel.CityViewModel

/**
 * Root für den POI-Tab.
 *
 * Spieler:
 *  - Kategorien → POIs → Detail
 *
 * Spielleiter:
 *  - Kategorien bearbeiten
 *  - POIs anlegen / bearbeiten
 *
 * KEIN NavController, reiner State-Flow.
 */
@Composable
fun PoiTab(
    cityViewModel: CityViewModel,
    factions: List<Faction>,
    accessContext: AccessContext
) {
    val context = LocalContext.current

    // ✅ Rollenfreies ViewModel, Factory ohne AccessContext
    val categoryViewModel: PoiCategoryViewModel = viewModel(
        factory = PoiCategoryViewModelFactory(
            context = context
        )
    )

    val allPois by cityViewModel.allPois.collectAsState(initial = emptyList())

    if (accessContext.canEdit()) {
        /* -------- Spielleiter -------- */

        GameMasterCategoryListScreen(
            categoryViewModel = categoryViewModel,
            cityViewModel = cityViewModel,
            allPois = allPois
        )

    } else {
        /* -------- Spieler -------- */

        PlayerCategoryListScreen(
            cityViewModel = cityViewModel,
            categoryViewModel = categoryViewModel,
            factions = factions
        )
    }
}
