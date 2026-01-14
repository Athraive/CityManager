package de.geier.citymanager.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import de.geier.citymanager.data.entity.CityDistrictEntity
import de.geier.citymanager.data.repository.CityDistrictRepository
import de.geier.citymanager.data.repository.PoiCategoryRepository
import de.geier.citymanager.data.repository.PointOfInterestRepository
import de.geier.citymanager.ui.Faction
import de.geier.citymanager.ui.FactionRepository
import de.geier.citymanager.ui.PoiCategory
import de.geier.citymanager.ui.PointOfInterest
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class CityViewModel(
    private val categoryRepository: PoiCategoryRepository,
    private val poiRepository: PointOfInterestRepository,
    private val factionRepository: FactionRepository,
    private val cityDistrictRepository: CityDistrictRepository
) : ViewModel() {

    /* ---------------- Kategorien ---------------- */

    val categories: StateFlow<List<PoiCategory>> =
        categoryRepository.categories
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

    /* ---------------- POIs (SL) ---------------- */

    val allPois: StateFlow<List<PointOfInterest>> =
        poiRepository.getAll()
            .stateIn(
                viewModelScope,
                SharingStarted.WhileSubscribed(5_000),
                emptyList()
            )

    /* ---------------- POIs (Spieler, erzwingend) ---------------- */

    val visiblePoisForPlayer: StateFlow<List<PointOfInterest>> =
        poiRepository.getVisibleForPlayer()
            .stateIn(
                viewModelScope,
                SharingStarted.WhileSubscribed(5_000),
                emptyList()
            )

    fun visiblePoisForPlayerByCategory(
        categoryId: String
    ): StateFlow<List<PointOfInterest>> =
        poiRepository.getVisibleForPlayerByCategory(categoryId)
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

    val factions: StateFlow<List<Faction>> =
        factionRepository.factions
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

    /* ---------------- Stadtviertel ---------------- */

    fun districtsForCity(
        cityId: String
    ): StateFlow<List<CityDistrictEntity>> =
        cityDistrictRepository
            .getDistrictsForCity(cityId)
            .stateIn(
                viewModelScope,
                SharingStarted.WhileSubscribed(5_000),
                emptyList()
            )

    fun districtById(
        districtId: String
    ): StateFlow<CityDistrictEntity?> =
        cityDistrictRepository
            .getDistrictById(districtId)
            .stateIn(
                viewModelScope,
                SharingStarted.WhileSubscribed(5_000),
                null
            )

    fun saveDistrict(district: CityDistrictEntity) {
        viewModelScope.launch {
            cityDistrictRepository.save(district)
        }
    }

    fun deleteDistrict(districtId: String) {
        viewModelScope.launch {
            cityDistrictRepository.delete(districtId)
        }
    }
}
