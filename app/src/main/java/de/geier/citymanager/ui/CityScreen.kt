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
import de.geier.citymanager.data.DatabaseProvider
import de.geier.citymanager.data.repository.PersonFactionRepository
import de.geier.citymanager.data.repository.PersonPoiRepository
import de.geier.citymanager.data.repository.PoiFactionRepository
import de.geier.citymanager.ui.components.AppTopBar
import de.geier.citymanager.ui.viewmodel.CityViewModel
import de.geier.citymanager.ui.viewmodel.PersonViewModel
import de.geier.citymanager.ui.viewmodel.PoiViewModel
import de.geier.citymanager.ui.PersonViewModelFactory
import de.geier.citymanager.ui.viewmodel.PoiViewModelFactory
import kotlinx.coroutines.flow.first

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
    val database = DatabaseProvider.getDatabase(context)

    val personFactionRepo = PersonFactionRepository(database.personFactionDao())
    val personPoiRepo = PersonPoiRepository(database.personPoiDao())
    val poiFactionRepo = PoiFactionRepository(database.poiFactionDao())

    var activeTab by remember { mutableStateOf(CityTab.CITY) }
    var activeDetail by remember { mutableStateOf<DetailTarget?>(null) }

    val personViewModel: PersonViewModel = viewModel(
        factory = PersonViewModelFactory(context, accessContext)
    )

    val poiViewModel: PoiViewModel = viewModel(
        factory = PoiViewModelFactory(context, accessContext)
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
                        personViewModel.clearSelection()
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

                        LaunchedEffect(person.id) {
                            personViewModel.selectPerson(person)
                        }

                        val assignedPoiIds by personViewModel.poiIdsForSelectedPerson.collectAsState()
                        val assignedFactionIds by personViewModel.factionIdsForSelectedPerson.collectAsState()

                        val assignedPois = allPois.filter { it.id in assignedPoiIds }
                        val assignedFactions = factions.filter { it.id in assignedFactionIds }

                        PersonDetailScreen(
                            person = person,
                            assignedPois = assignedPois,
                            assignedFactions = assignedFactions,
                            accessContext = accessContext,
                            onBack = { activeDetail = null },
                            onSave = { personViewModel.save(it) },
                            onDelete = { personViewModel.delete(it) },
                            onAssignPois = {},
                            onAssignFactions = {},
                            onPersonClick = { activeDetail = DetailTarget.Person(it) },
                            onPoiClick = { activeDetail = DetailTarget.Poi(it) },
                            onFactionClick = { activeDetail = DetailTarget.Faction(it) }
                        )
                    }
                }

                /* ================= POI DETAIL ================= */

                is DetailTarget.Poi -> {

                    val poi = allPois.firstOrNull { it.id == detail.id }

                    if (poi != null) {

                        val assignedFactionIds by produceState(initialValue = emptyList<String>(), key1 = poi.id) {
                            value = poiFactionRepo.getFactionIdsForPoi(poi.id).first()
                        }

                        val assignedPersonIds by produceState(initialValue = emptyList<String>(), key1 = poi.id) {
                            value = personPoiRepo.getPersonIdsForPoi(poi.id).first()
                        }

                        val assignedFactions = factions.filter { it.id in assignedFactionIds }
                        val assignedPersons = allPersons.filter { it.id in assignedPersonIds }

                        PoiDetailScreen(
                            poi = poi,
                            assignedFactions = assignedFactions,
                            assignedPersons = assignedPersons,
                            accessContext = accessContext,
                            onBack = { activeDetail = null },
                            onSave = { cityViewModel.savePoi(it) },
                            onDelete = { cityViewModel.deletePoi(it) },
                            onAssignFactions = {},
                            onPersonClick = { activeDetail = DetailTarget.Person(it) },
                            onFactionClick = { activeDetail = DetailTarget.Faction(it) }
                        )
                    }
                }

                /* ================= FACTION DETAIL ================= */

                is DetailTarget.Faction -> {

                    val faction = factions.firstOrNull { it.id == detail.id }

                    if (faction != null) {

                        val assignedPersons by produceState(initialValue = emptyList<Person>(), key1 = faction.id) {
                            val result = mutableListOf<Person>()
                            allPersons.forEach { person ->
                                val ids = personFactionRepo.getFactionIdsForPerson(person.id).first()
                                if (faction.id in ids) result.add(person)
                            }
                            value = result
                        }

                        val assignedPois by produceState(initialValue = emptyList<PointOfInterest>(), key1 = faction.id) {
                            val result = mutableListOf<PointOfInterest>()
                            allPois.forEach { poi ->
                                val ids = poiFactionRepo.getFactionIdsForPoi(poi.id).first()
                                if (faction.id in ids) result.add(poi)
                            }
                            value = result
                        }

                        FactionDetailScreen(
                            faction = faction,
                            assignedPersons = assignedPersons,
                            assignedPois = assignedPois,
                            accessContext = accessContext,
                            onBack = { activeDetail = null },
                            onSave = { cityViewModel.saveFaction(it) },
                            onDelete = { cityViewModel.deleteFaction(it) },
                            onPersonClick = { activeDetail = DetailTarget.Person(it) },
                            onPoiClick = { activeDetail = DetailTarget.Poi(it) }
                        )
                    }
                }

                /* ================= NORMALER TAB ================= */

                null -> {

                    when (activeTab) {

                        CityTab.CITY -> CityOverviewTab(cityViewModel, accessContext)

                        CityTab.PERSONS -> PersonenTab(
                            viewModel = personViewModel,
                            factions = factions,
                            pois = allPois,
                            categories = categories,
                            accessContext = accessContext,
                            onFactionLinkClicked = { activeDetail = DetailTarget.Faction(it) }
                        )

                        CityTab.POIS -> PoiTab(
                            cityViewModel = cityViewModel,
                            factions = factions,
                            accessContext = accessContext
                        )

                        CityTab.FACTIONS -> FraktionenTab(
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
