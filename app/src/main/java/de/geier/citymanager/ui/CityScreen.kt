@file:OptIn(ExperimentalMaterial3Api::class)

package de.geier.citymanager.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
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

sealed class DetailTarget {
    data class Person(val id: String) : DetailTarget()
    data class Poi(val id: String) : DetailTarget()
    data class Faction(val id: String) : DetailTarget()
}

@Composable
fun CityScreen(
    cityViewModel: CityViewModel,
    accessContext: AccessContext
) {

    val context = LocalContext.current

    var activeTab by remember { mutableStateOf(CityTab.CITY) }
    var activeDetail by remember { mutableStateOf<DetailTarget?>(null) }

    val personViewModel: PersonViewModel = viewModel(
        factory = PersonViewModelFactory(
            context = context,
            accessContext = accessContext
        )
    )

    val factions by cityViewModel.factions.collectAsState()
    val allPois by cityViewModel.allPois.collectAsState()
    val allPersons by cityViewModel.allPersons.collectAsState()
    val categories by cityViewModel.poiCategories.collectAsState()

    Scaffold(
        topBar = {
            AppTopBar(
                title = "CityManager",
                accessContext = accessContext
            )
        },
        bottomBar = {
            if (activeDetail == null) {
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
        }
    ) { padding ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {

            when (val detail = activeDetail) {

                /* ================= PERSON DETAIL ================= */

                is DetailTarget.Person -> {

                    val person = allPersons.firstOrNull { it.id == detail.id }

                    if (person != null) {

                        PersonDetailScreen(
                            person = person,
                            assignedPois = emptyList(),
                            assignedFactions = emptyList(),
                            accessContext = accessContext,
                            onBack = { activeDetail = null },
                            onSave = { personViewModel.save(it) },
                            onDelete = { personViewModel.delete(it) },
                            onAssignPois = {},
                            onAssignFactions = {},
                            onPersonClick = { id ->
                                activeDetail = DetailTarget.Person(id)
                            },
                            onPoiClick = { id ->
                                activeDetail = DetailTarget.Poi(id)
                            },
                            onFactionClick = { id ->
                                activeDetail = DetailTarget.Faction(id)
                            }
                        )
                    }
                }

                /* ================= FACTION DETAIL ================= */

                is DetailTarget.Faction -> {

                    val faction =
                        factions.firstOrNull { it.id == detail.id }

                    if (faction != null) {

                        FactionDetailScreen(
                            faction = faction,
                            assignedPersons = emptyList(),
                            assignedPois = emptyList(),
                            accessContext = accessContext,
                            onBack = { activeDetail = null },
                            onSave = { cityViewModel.saveFaction(it) },
                            onDelete = { cityViewModel.deleteFaction(it) },
                            onPersonClick = { id ->
                                activeDetail = DetailTarget.Person(id)
                            },
                            onPoiClick = { id ->
                                activeDetail = DetailTarget.Poi(id)
                            }
                        )
                    }
                }

                /* ================= POI DETAIL ================= */

                is DetailTarget.Poi -> {
                    activeDetail = null
                    activeTab = CityTab.POIS
                }

                /* ================= NORMALER TAB ================= */

                null -> {

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
                                onFactionLinkClicked = { id ->
                                    activeDetail = DetailTarget.Faction(id)
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
                                persons = allPersons,
                                pois = allPois,
                                onSave = { cityViewModel.saveFaction(it) },
                                onDelete = { cityViewModel.deleteFaction(it) }
                            )
                        }
                    }
                }
            }
        }
    }
}
