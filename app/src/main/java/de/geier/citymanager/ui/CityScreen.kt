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
import de.geier.citymanager.ui.viewmodel.FactionViewModel
import de.geier.citymanager.ui.viewmodel.FactionViewModelFactory

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

    // ✅ EINZIGE Quelle für Fraktionen
    val factionViewModel: FactionViewModel = viewModel(
        factory = FactionViewModelFactory()
    )

    /* ---------------- Gemeinsame States ---------------- */

    val factions by factionViewModel.factions.collectAsState()

    /* ---------------- Layout ---------------- */

    Scaffold(
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
                    CityOverviewTab(
                        cityViewModel = cityViewModel
                    )
                }

                CityTab.PERSONS -> {
                    PersonenTab(
                        viewModel = personViewModel,
                        factions = factions,      // ✅ jetzt dynamisch
                        pois = emptyList(),
                        categories = emptyList(),
                        isGameMaster = isGameMaster
                    )
                }

                CityTab.POIS -> {
                    PoiTab(
                        cityViewModel = cityViewModel,
                        personViewModel = personViewModel,
                        isGameMaster = isGameMaster
                    )
                }

                CityTab.FACTIONS -> {
                    // ✅ KEIN eigenes ViewModel mehr im Tab
                    FraktionenTab(
                        isGameMaster = isGameMaster
                    )
                }
            }
        }
    }
}
