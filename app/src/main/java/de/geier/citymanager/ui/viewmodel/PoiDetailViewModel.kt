package de.geier.citymanager.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import de.geier.citymanager.data.repository.PersonPoiRepository
import de.geier.citymanager.data.repository.PointOfInterestRepository
import de.geier.citymanager.ui.PointOfInterest
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

/**
 * ViewModel für POI-Details.
 *
 * - Enthält KEINE UI- oder Rollenlogik
 * - Spiegelt PersonViewModel logisch
 * - Kapselt Person ↔ POI Zuweisungen
 */
class PoiDetailViewModel(
    private val poiRepository: PointOfInterestRepository,
    private val personPoiRepository: PersonPoiRepository
) : ViewModel() {

    /* ---------------- Auswahl ---------------- */

    private val _selectedPoi = MutableStateFlow<PointOfInterest?>(null)
    val selectedPoi: StateFlow<PointOfInterest?> = _selectedPoi.asStateFlow()

    fun selectPoi(poi: PointOfInterest) {
        _selectedPoi.value = poi
    }

    fun clearSelection() {
        _selectedPoi.value = null
    }

    /* ---------------- Persistenz ---------------- */

    fun save(poi: PointOfInterest) {
        viewModelScope.launch {
            poiRepository.save(poi)

            if (_selectedPoi.value?.id == poi.id) {
                _selectedPoi.value = poi
            }
        }
    }

    fun delete(poi: PointOfInterest) {
        viewModelScope.launch {
            poiRepository.delete(poi)
            clearSelection()
        }
    }

    /* ---------------- Personen-Zuordnungen ---------------- */

    val personIdsForSelectedPoi: StateFlow<Set<String>> =
        selectedPoi
            .flatMapLatest { poi ->
                if (poi == null) {
                    flowOf(emptyList())
                } else {
                    personPoiRepository.getPersonIdsForPoi(poi.id)
                }
            }
            .map { it.toSet() }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = emptySet()
            )

    fun togglePersonAssignment(personId: String) {
        val poi = selectedPoi.value ?: return
        val current = personIdsForSelectedPoi.value

        viewModelScope.launch {
            if (current.contains(personId)) {
                personPoiRepository.removePoiFromPerson(
                    personId = personId,
                    poiId = poi.id
                )
            } else {
                personPoiRepository.addPoiToPerson(
                    personId = personId,
                    poiId = poi.id
                )
            }
        }
    }
}
