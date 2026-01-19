package de.geier.citymanager.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import de.geier.citymanager.ui.viewmodel.CityViewModel
import de.geier.citymanager.ui.viewmodel.PersonViewModel
import de.geier.citymanager.ui.viewmodel.FactionViewModel
import de.geier.citymanager.ui.viewmodel.FactionViewModelFactory

/**
 * Root für den POI-Tab.
 *
 * Spieler:
 *  - Kategorien → POIs → Detail (read-only)
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
    personViewModel: PersonViewModel,
    isGameMaster: Boolean
) {
    val context = LocalContext.current

    val categoryViewModel: PoiCategoryViewModel = viewModel(
        factory = PoiCategoryViewModelFactory(context)
    )

    // ✅ zentrale Fraktionen (identisch zu CityScreen)
    val factionViewModel: FactionViewModel = viewModel(
        factory = FactionViewModelFactory()
    )
    val factions by factionViewModel.factions.collectAsState()

    // ✔ expliziter Import für Release-Build
    val allPois by cityViewModel.allPois.collectAsState(initial = emptyList())

    if (isGameMaster) {
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
            persons = emptyList(),
            factions = factions,          // ✅ DURCHGEREICHT
            personViewModel = personViewModel
        )
    }
}
