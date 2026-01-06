package de.geier.citymanager.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import de.geier.citymanager.data.repository.PoiCategoryRepository
import de.geier.citymanager.data.repository.PointOfInterestRepository
import de.geier.citymanager.ui.Faction
import de.geier.citymanager.ui.FactionRepository
import de.geier.citymanager.ui.PoiCategory
import de.geier.citymanager.ui.PointOfInterest
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class CityViewModel(
    private val categoryRepository: PoiCategoryRepository,
    private val poiRepository: PointOfInterestRepository,
    private val factionRepository: FactionRepository
) : ViewModel() {

    /* ---------------- Kategorien ---------------- */

    val categories = categoryRepository.categories
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5_000),
            emptyList()
        )

    fun saveCategory(category: PoiCategory) {
        viewModelScope.launch {
            categoryRepository.save(category)
        }
    }

    fun deleteCategory(category: PoiCategory) {
        viewModelScope.launch {
            categoryRepository.delete(category)
        }
    }

    /* ---------------- POIs ---------------- */

    val allPois = poiRepository.getAll()
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5_000),
            emptyList()
        )

    fun savePoi(poi: PointOfInterest) {
        viewModelScope.launch {
            poiRepository.save(poi)
        }
    }

    fun deletePoi(poi: PointOfInterest) {
        viewModelScope.launch {
            poiRepository.delete(poi)
        }
    }

    /* ---------------- Fraktionen ---------------- */

    val factions = factionRepository.factions
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5_000),
            emptyList()
        )

    fun saveFaction(faction: Faction) {
        factionRepository.save(faction)
    }

    fun deleteFaction(factionId: String) {
        factionRepository.delete(factionId)
    }
}
