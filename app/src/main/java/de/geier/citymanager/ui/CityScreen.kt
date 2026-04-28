@file:OptIn(ExperimentalMaterial3Api::class)

package de.geier.citymanager.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.runtime.CompositionLocalProvider
import de.geier.citymanager.data.DatabaseProvider
import de.geier.citymanager.data.entity.CityEntity
import de.geier.citymanager.data.repository.PersonFactionRepository
import de.geier.citymanager.data.repository.PersonPoiRepository
import de.geier.citymanager.data.repository.PoiFactionRepository
import de.geier.citymanager.ui.components.AppTopBar
import de.geier.citymanager.ui.viewmodel.CityViewModel
import de.geier.citymanager.ui.viewmodel.PersonViewModel
import de.geier.citymanager.ui.viewmodel.PoiViewModel
import de.geier.citymanager.ui.PersonViewModelFactory
import de.geier.citymanager.ui.viewmodel.PoiViewModelFactory
import de.geier.citymanager.ui.theme.*
import kotlinx.coroutines.flow.first
import de.geier.citymanager.ui.map.MapViewModel
import de.geier.citymanager.ui.map.MapViewModelFactory
import androidx.navigation.compose.rememberNavController
import de.geier.citymanager.ui.theme.toColorScheme

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

    val cityEntity by produceState<CityEntity?>(
        initialValue = null,
        key1 = accessContext.cityId
    ) {
        value = database.cityDao()
            .getCityById(accessContext.cityId)
            .first()
    }

    val cityTheme = cityEntity?.toCityTheme()
        ?: CityTheme(
            stylePreset = CityStylePreset.SCIFI,
            backgroundImageUri = null,
            fontPreset = FontPreset.DEFAULT
        )
    val colorScheme = cityTheme.toColorScheme()

    val typography = resolveTypography(
        base = MaterialTheme.typography,
        preset = cityTheme.fontPreset
    )

    val personFactionRepo = PersonFactionRepository(database.personFactionDao())
    val personPoiRepo = PersonPoiRepository(database.personPoiDao())
    val poiFactionRepo = PoiFactionRepository(database.poiFactionDao())

    var activeTab by remember { mutableStateOf(CityTab.CITY) }
    var activeDetail by remember { mutableStateOf<DetailTarget?>(null) }

    var focusPersonId by remember { mutableStateOf<String?>(null) }
    var focusPoiId by remember { mutableStateOf<String?>(null) }
    var mapFocusTick by remember { mutableStateOf(0) }

    val cityNavController = rememberNavController()

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

    CompositionLocalProvider(
        LocalCityTheme provides cityTheme
    ) {

        MaterialTheme(
            colorScheme = colorScheme,
            typography = typography
        ) {

            CompositionLocalProvider(
                LocalContentColor provides MaterialTheme.colorScheme.onBackground
            ) {

                Scaffold(
                    containerColor = Color.Transparent,
                    topBar = {
                        AppTopBar(
                            title = cityEntity?.name ?: "CityManager",
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

                            is DetailTarget.Person -> {
                                val person = allPersons.firstOrNull { it.id == detail.id }

                                if (person != null) {

                                    val assignedPoiIds by produceState(
                                        initialValue = emptyList<String>(),
                                        key1 = person.id
                                    ) {
                                        value = personPoiRepo.getPoiIdsForPerson(person.id).first()
                                    }

                                    val assignedFactionIds by produceState(
                                        initialValue = emptyList<String>(),
                                        key1 = person.id
                                    ) {
                                        value =
                                            personFactionRepo.getFactionIdsForPerson(person.id)
                                                .first()
                                    }

                                    PersonDetailScreen(
                                        person = person,
                                        assignedPois = allPois.filter { it.id in assignedPoiIds },
                                        assignedFactions = factions.filter { it.id in assignedFactionIds },
                                        accessContext = accessContext,
                                        onBack = { activeDetail = null },
                                        onSave = { personViewModel.save(it) },
                                        onDelete = { personViewModel.delete(it) },
                                        onAssignPois = {},
                                        onAssignFactions = {},
                                        onPersonClick = { activeDetail = DetailTarget.Person(it) },
                                        onPoiClick = { activeDetail = DetailTarget.Poi(it) },
                                        onFactionClick = {
                                            activeDetail = DetailTarget.Faction(it)
                                        },
                                        onShowOnMap = { id ->
                                            activeDetail = null
                                            focusPoiId = null
                                            focusPersonId = id
                                            mapFocusTick++
                                            activeTab = CityTab.CITY
                                        }
                                    )
                                }
                            }

                            is DetailTarget.Poi -> {
                                val poi = allPois.firstOrNull { it.id == detail.id }

                                if (poi != null) {

                                    val assignedFactionIds by produceState(
                                        initialValue = emptyList<String>(),
                                        key1 = poi.id
                                    ) {
                                        value = poiFactionRepo.getFactionIdsForPoi(poi.id).first()
                                    }

                                    val assignedPersonIds by produceState(
                                        initialValue = emptyList<String>(),
                                        key1 = poi.id
                                    ) {
                                        value = personPoiRepo.getPersonIdsForPoi(poi.id).first()
                                    }

                                    PoiDetailScreen(
                                        poi = poi,
                                        assignedFactions = factions.filter { it.id in assignedFactionIds },
                                        assignedPersons = allPersons.filter { it.id in assignedPersonIds },
                                        accessContext = accessContext,
                                        onBack = { activeDetail = null },
                                        onSave = { cityViewModel.savePoi(it) },
                                        onDelete = { cityViewModel.deletePoi(it) },
                                        onAssignFactions = {},
                                        onPersonClick = { activeDetail = DetailTarget.Person(it) },
                                        onFactionClick = {
                                            activeDetail = DetailTarget.Faction(it)
                                        },
                                        onShowOnMap = { id ->
                                            activeDetail = null
                                            focusPersonId = null
                                            focusPoiId = id
                                            mapFocusTick++
                                            activeTab = CityTab.CITY
                                        }
                                    )
                                }
                            }

                            is DetailTarget.Faction -> {}

                            null -> {
                                when (activeTab) {

                                    CityTab.CITY -> {
                                        val mapViewModel: MapViewModel = viewModel(
                                            factory = MapViewModelFactory(
                                                cityViewModel = cityViewModel,
                                                personFactionRepository = personFactionRepo,
                                                poiFactionRepository = poiFactionRepo
                                            )
                                        )

                                        CityOverviewTab(
                                            cityViewModel = cityViewModel,
                                            accessContext = accessContext,
                                            mapViewModel = mapViewModel,
                                            focusPersonId = focusPersonId,
                                            focusPoiId = focusPoiId,
                                            focusTrigger = mapFocusTick,
                                            navController = cityNavController,
                                            onPersonBubbleClick = {
                                                activeDetail = DetailTarget.Person(it)
                                            },
                                            onPoiBubbleClick = {
                                                activeDetail = DetailTarget.Poi(it)
                                            }
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
                                                activeDetail = DetailTarget.Faction(it)
                                            },
                                            onShowOnMap = { id ->
                                                activeDetail = null
                                                focusPersonId = id
                                                focusPoiId = null
                                                mapFocusTick++
                                                activeTab = CityTab.CITY
                                            }
                                        )
                                    }

                                    CityTab.POIS -> {
                                        PoiTab(
                                            cityViewModel = cityViewModel,
                                            factions = factions,
                                            accessContext = accessContext,
                                            onShowOnMap = { id ->
                                                activeDetail = null
                                                focusPoiId = id
                                                focusPersonId = null
                                                mapFocusTick++
                                                activeTab = CityTab.CITY
                                            }
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
        }
    }
}