@file:OptIn(ExperimentalMaterial3Api::class)

package de.geier.citymanager.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import de.geier.citymanager.ui.components.AppTopBar
import de.geier.citymanager.ui.viewmodel.CityViewModel
import de.geier.citymanager.ui.viewmodel.PersonViewModel
import de.geier.citymanager.ui.PersonViewModelFactory

enum class CityTab {
    CITY,
    PERSONS,
    POIS,
    FACTIONS
}

@Composable
fun CityScreen(
    cityViewModel: CityViewModel,
    accessContext: AccessContext
) {
    val context = LocalContext.current

    var activeTab by remember { mutableStateOf(CityTab.CITY) }

    val personViewModel: PersonViewModel = viewModel(
        factory = PersonViewModelFactory(
            context = context,
            accessContext = accessContext
        )
    )

    /* ---------------- Gemeinsame States ---------------- */

    val factions by cityViewModel.factions.collectAsState()
    val allPois by cityViewModel.allPois.collectAsState()
    val allPersons by cityViewModel.allPersons.collectAsState()   // 🔹 neu
    val categories by cityViewModel.poiCategories.collectAsState()

    Scaffold(
        topBar = {
            AppTopBar(
                title = "CityManager",
                accessContext = accessContext
            )
        },
        bottomBar = {
            CityBottomBar(
                activeTab = activeTab,
                accessContext = accessContext,
                onTabSelected = { tab ->
                    activeTab = tab
                    if (tab == CityTab.PERSONS) {
                        personViewModel.clearSelection()
                    }
                }
            )
        }
    ) { padding ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            when (activeTab) {

                CityTab.CITY -> {
                    CityOverviewTab(
                        cityViewModel = cityViewModel,
                        accessContext = accessContext
                    )
                }

                CityTab.PERSONS -> {
                    PersonenTab(
                        viewModel = personViewModel,
                        factions = factions,
                        pois = allPois,
                        categories = categories,
                        accessContext = accessContext,
                        onFactionLinkClicked = {
                            activeTab = CityTab.FACTIONS
                        }
                    )
                }

                CityTab.POIS -> {
                    PoiTab(
                        cityViewModel = cityViewModel,
                        factions = factions,
                        accessContext = accessContext
                    )
                }

                CityTab.FACTIONS -> {
                    FraktionenTab(
                        accessContext = accessContext,
                        factions = factions,
                        persons = allPersons,        // 🔹 neu
                        pois = allPois,              // 🔹 neu
                        onSave = { cityViewModel.saveFaction(it) },
                        onDelete = { cityViewModel.deleteFaction(it) }
                    )
                }
            }
        }
    }
}
