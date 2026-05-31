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

@Composable
fun PoiTab(
    cityViewModel: CityViewModel,
    factions: List<Faction>,
    accessContext: AccessContext,
    onPersonLinkClicked: (String) -> Unit,
    onFactionLinkClicked: (String) -> Unit,
    onShowOnMap: (String) -> Unit
) {
    val context = LocalContext.current

    val categoryViewModel: PoiCategoryViewModel = viewModel(
        factory = PoiCategoryViewModelFactory(
            context = context,
            accessContext = accessContext
        )
    )

    val poiViewModel: PoiViewModel = viewModel(
        factory = PoiViewModelFactory(
            context = context,
            accessContext = accessContext
        )
    )

    val allPois by cityViewModel
        .allPois
        .collectAsState(initial = emptyList())

    val allPersons by cityViewModel
        .allPersons
        .collectAsState(initial = emptyList())

    val visibleFactionsForPlayer =
        factions.filter { it.visible }

    /* 🔥 HIER: Event doppeln */
    val triggerShowOnMap: (String) -> Unit = { id ->
        onShowOnMap(id)
        onShowOnMap(id)
    }

    if (accessContext.canEdit()) {

        GameMasterCategoryListScreen(
            categoryViewModel = categoryViewModel,
            cityViewModel = cityViewModel,
            poiViewModel = poiViewModel,
            allPois = allPois,
            factions = factions,
            persons = allPersons,
            accessContext = accessContext,

            onPersonLinkClicked = onPersonLinkClicked,

            onFactionLinkClicked = onFactionLinkClicked,

            onShowOnMap = triggerShowOnMap
        )

    } else {

        PlayerCategoryListScreen(
            cityViewModel = cityViewModel,
            categoryViewModel = categoryViewModel,
            factions = visibleFactionsForPlayer,
            accessContext = accessContext,
            onShowOnMap = triggerShowOnMap   // 🔥 geändert
        )
    }
}