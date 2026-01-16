package de.geier.citymanager.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import de.geier.citymanager.ui.viewmodel.CityViewModel
import de.geier.citymanager.ui.viewmodel.PersonViewModel

/**
 * Zentrale Tab-Definition für die CityScreen-Navigation.
 * Jeder Tab ist ein logischer Root.
 */
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
    /* ---------------- Tab-State ---------------- */

    var activeTab by remember { mutableStateOf(CityTab.CITY) }

    /* ---------------- ViewModels ---------------- */

    val context = LocalContext.current

    val personViewModel: PersonViewModel = viewModel(
        factory = PersonViewModelFactory(context)
    )

    /* ---------------- Layout ---------------- */

    Scaffold(
        bottomBar = {
            CityBottomBar(
                activeTab = activeTab,
                isGameMaster = isGameMaster,
                onTabSelected = { tab ->
                    activeTab = tab

                    // expliziter Reset beim Tab-Wechsel
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

                /* ---------------- Über die Stadt ---------------- */

                CityTab.CITY -> {
                    CityOverviewTab(
                        cityViewModel = cityViewModel
                    )
                }

                /* ---------------- Personen ---------------- */

                CityTab.PERSONS -> {
                    PersonenTab(
                        viewModel = personViewModel,
                        pois = emptyList(),
                        categories = emptyList(),
                        isGameMaster = isGameMaster
                    )
                }

                /* ---------------- POIs ---------------- */

                CityTab.POIS -> {
                    PoiTab(
                        cityViewModel = cityViewModel,
                        personViewModel = personViewModel,
                        isGameMaster = isGameMaster
                    )
                }

                /* ---------------- Fraktionen ---------------- */

                CityTab.FACTIONS -> {
                    FraktionenTab(
                        cityViewModel = cityViewModel,
                        isGameMaster = isGameMaster
                    )
                }
            }
        }
    }
}
