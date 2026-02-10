@file:OptIn(kotlinx.coroutines.ExperimentalCoroutinesApi::class)

package de.geier.citymanager.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import de.geier.citymanager.data.repository.PointOfInterestRepository
import de.geier.citymanager.data.repository.PoiFactionRepository
import de.geier.citymanager.ui.AccessContext
import de.geier.citymanager.ui.PointOfInterest
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class PoiViewModel(
    private val accessContext: AccessContext,
    private val poiRepository: PointOfInterestRepository,
    private val poiFactionRepository: PoiFactionRepository
) : ViewModel() {

    /* ---------------- POIs ---------------- */

    val pois: StateFlow<List<PointOfInterest>> =
        poiRepository
            .getPois(accessContext)
            .stateIn(
                viewModelScope,
                SharingStarted.WhileSubscribed(5_000),
                emptyList()
            )

    /* ---------------- Auswahl ---------------- */

    private val _selectedPoi = MutableStateFlow<PointOfInterest?>(null)
    val selectedPoi: StateFlow<PointOfInterest?> = _selectedPoi.asStateFlow()

    fun selectPoi(poi: PointOfInterest) {
        _selectedPoi.value = poi
    }

    fun clearSelection() {
        _selectedPoi.value = null
    }

    /* ---------------- Fraktions-Zuweisungen ---------------- */

    val factionIdsForSelectedPoi: StateFlow<Set<String>> =
        selectedPoi
            .flatMapLatest { poi ->
                if (poi == null) {
                    flowOf(emptyList())
                } else {
                    poiFactionRepository.getFactionIdsForPoi(poi.id)
                }
            }
            .map { it.toSet() }
            .stateIn(
                viewModelScope,
                SharingStarted.WhileSubscribed(5_000),
                emptySet()
            )

    fun toggleFactionAssignment(factionId: String) {
        val poi = selectedPoi.value ?: return

        // 🔒 Spieler dürfen keine Beziehungen ändern
        if (!accessContext.canEdit()) return

        val current = factionIdsForSelectedPoi.value

        viewModelScope.launch {
            if (current.contains(factionId)) {
                poiFactionRepository.removeFactionFromPoi(poi.id, factionId)
            } else {
                poiFactionRepository.addFactionToPoi(poi.id, factionId)
            }
        }
    }
}
