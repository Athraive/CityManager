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
    isGameMaster: Boolean
) {
    /* ---------------- Context ---------------- */

    val context = LocalContext.current

    /* ---------------- Tab-State ---------------- */

    var activeTab by remember { mutableStateOf(CityTab.CITY) }

    /* ---------------- ViewModels ---------------- */

    val personViewModel: PersonViewModel = viewModel(
        factory = PersonViewModelFactory(context)
    )

    /* ---------------- Gemeinsame States ---------------- */

    val factions by cityViewModel.factions.collectAsState()

    /* ---------------- Layout ---------------- */

    Scaffold(
        topBar = {
            AppTopBar(
                title = "CityManager",
                isGameMaster = isGameMaster
            )
        },
        bottomBar = {
            CityBottomBar(
                activeTab = activeTab,
                isGameMaster = isGameMaster,
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
                    CityOverviewTab(cityViewModel)
                }

                CityTab.PERSONS -> {
                    PersonenTab(
                        viewModel = personViewModel,
                        factions = factions,
                        pois = emptyList(),
                        categories = emptyList(),
                        isGameMaster = isGameMaster
                    )
                }

                CityTab.POIS -> {
                    PoiTab(
                        cityViewModel = cityViewModel,
                        factions = factions,
                        isGameMaster = isGameMaster
                    )
                }

                CityTab.FACTIONS -> {
                    FraktionenTab(
                        isGameMaster = isGameMaster,
                        factions = factions,
                        onSave = { cityViewModel.saveFaction(it) },
                        onDelete = { cityViewModel.deleteFaction(it) }
                    )
                }
            }
        }
    }
}
