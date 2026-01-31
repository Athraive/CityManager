package de.geier.citymanager.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import de.geier.citymanager.ui.viewmodel.CityViewModel
import de.geier.citymanager.ui.PoiCategoryViewModel
import de.geier.citymanager.ui.PoiCategoryViewModelFactory


/**
 * Root für den POI-Tab.
 *
 * Einheitlicher Einstieg für Spieler und Spielleiter.
 * Rollenlogik erfolgt ausschließlich über AccessContext
 * innerhalb der nachgelagerten Screens.
 */
@Composable
fun PoiTab(
    cityViewModel: CityViewModel,
    factions: List<Faction>,
    accessContext: AccessContext
) {
    val context = LocalContext.current

    // Rollenfreies Category-ViewModel
    val categoryViewModel: PoiCategoryViewModel = viewModel(
        factory = PoiCategoryViewModelFactory(
            context = context
        )
    )

    // Aktuell noch nicht genutzt, aber bewusst hier gelassen
    // für kommende Schritte (POI-Listen / Details)
    val allPois by cityViewModel.allPois.collectAsState(initial = emptyList())

    // 🔑 EINZIGER Entry-Point für Kategorien
    CategoryListScreen(
        categoryViewModel = categoryViewModel,
        cityViewModel = cityViewModel,
        factions = factions,
        accessContext = accessContext
    )
}
