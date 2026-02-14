@file:OptIn(ExperimentalMaterial3Api::class)

package de.geier.citymanager.ui

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import de.geier.citymanager.ui.viewmodel.CityViewModel
import de.geier.citymanager.ui.viewmodel.PoiViewModel
import de.geier.citymanager.ui.viewmodel.PoiViewModelFactory

/**
 * Root für den POI-Tab.
 *
 * – SL: voller Workflow (PoiViewModel)
 * – Player: read-only Anzeige
 *
 * KEINE Player-spezifischen ViewModels.
 */
@Composable
fun PoiTab(
    cityViewModel: CityViewModel,
    factions: List<Faction>,
    accessContext: AccessContext
) {
    val context = LocalContext.current

    /* ---------------- Kategorie-VM (rollenfrei) ---------------- */

    val categoryViewModel: PoiCategoryViewModel = viewModel(
        factory = PoiCategoryViewModelFactory(
            context = context,
            accessContext = accessContext
        )
    )

    /* ---------------- POI-Interaktions-VM (nur SL) ---------------- */

    val poiViewModel: PoiViewModel = viewModel(
        factory = PoiViewModelFactory(
            context = context,
            accessContext = accessContext
        )
    )

    /* ---------------- Daten ---------------- */

    val allPois by cityViewModel
        .allPois
        .collectAsState(initial = emptyList())

    val allPersons by cityViewModel
        .allPersons
        .collectAsState(initial = emptyList())

    // 🔒 Player darf nur sichtbare Fraktionen sehen
    val visibleFactionsForPlayer =
        factions.filter { it.visible }

    /* ---------------- Routing ---------------- */

    if (accessContext.canEdit()) {

        /* ---------- Spielleiter ---------- */

        GameMasterCategoryListScreen(
            categoryViewModel = categoryViewModel,
            cityViewModel = cityViewModel,
            poiViewModel = poiViewModel,
            allPois = allPois,
            factions = factions,
            persons = allPersons,
            accessContext = accessContext
        )

    } else {

        /* ---------- Spieler ---------- */

        PlayerCategoryListScreen(
            cityViewModel = cityViewModel,
            categoryViewModel = categoryViewModel,
            factions = visibleFactionsForPlayer,
            accessContext = accessContext
        )
    }
}
