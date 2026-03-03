package de.geier.citymanager.ui.map

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import de.geier.citymanager.data.repository.PersonFactionRepository
import de.geier.citymanager.data.repository.PoiFactionRepository
import de.geier.citymanager.ui.viewmodel.CityViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn

class MapViewModel(
    cityViewModel: CityViewModel,
    personFactionRepository: PersonFactionRepository,
    poiFactionRepository: PoiFactionRepository
) : ViewModel() {

    private val persons = cityViewModel.allPersons
    private val pois = cityViewModel.allPois

    private val personCrossRefs =
        personFactionRepository.getAll()

    private val poiCrossRefs =
        poiFactionRepository.getAll()

    val personsWithFactions: StateFlow<List<PersonWithFactions>> =
        combine(
            persons,
            personCrossRefs
        ) { personList, crossRefs ->

            val personMap =
                crossRefs.groupBy { it.personId }
                    .mapValues { entry ->
                        entry.value.map { it.factionId }
                    }

            personList.map { person ->
                PersonWithFactions(
                    person = person,
                    factionIds = personMap[person.id] ?: emptyList()
                )
            }
        }
            .stateIn(
                viewModelScope,
                SharingStarted.WhileSubscribed(5_000),
                emptyList()
            )

    val poisWithFactions: StateFlow<List<PoiWithFactions>> =
        combine(
            pois,
            poiCrossRefs
        ) { poiList, crossRefs ->

            val poiMap =
                crossRefs.groupBy { it.poiId }
                    .mapValues { entry ->
                        entry.value.map { it.factionId }
                    }

            poiList.map { poi ->
                PoiWithFactions(
                    poi = poi,
                    factionIds = poiMap[poi.id] ?: emptyList()
                )
            }
        }
            .stateIn(
                viewModelScope,
                SharingStarted.WhileSubscribed(5_000),
                emptyList()
            )
}