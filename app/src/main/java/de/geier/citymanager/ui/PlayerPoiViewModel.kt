package de.geier.citymanager.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import de.geier.citymanager.data.repository.PersonPoiRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class PlayerPoiViewModel(
    poiId: String,
    personPoiRepository: PersonPoiRepository
) : ViewModel() {

    /**
     * IDs aller Personen, die diesem POI zugeordnet sind.
     * Read-only, geeignet für Spieler- und SL-Sicht.
     */
    val personIdsForPoi: StateFlow<Set<String>> =
        personPoiRepository
            .getPersonIdsForPoi(poiId)
            .map { it.toSet() }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = emptySet()
            )
}
