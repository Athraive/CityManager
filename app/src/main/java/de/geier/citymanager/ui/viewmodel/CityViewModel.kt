package de.geier.citymanager.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import de.geier.citymanager.data.entity.CityDistrictEntity
import de.geier.citymanager.data.entity.CityLoreEntity
import de.geier.citymanager.data.repository.CityDistrictRepository
import de.geier.citymanager.data.repository.CityLoreRepository
import de.geier.citymanager.data.repository.PoiCategoryRepository
import de.geier.citymanager.data.repository.PointOfInterestRepository
import de.geier.citymanager.ui.Faction
import de.geier.citymanager.ui.FactionRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import de.geier.citymanager.ui.PointOfInterest

class CityViewModel(
    private val poiCategoryRepository: PoiCategoryRepository,
    private val poiRepository: PointOfInterestRepository,
    private val factionRepository: FactionRepository,
    private val cityDistrictRepository: CityDistrictRepository,
    private val cityLoreRepository: CityLoreRepository
) : ViewModel() {

    /* ---------------- Stadtgeschichte ---------------- */

    fun cityLore(cityId: String): StateFlow<CityLoreEntity?> =
        cityLoreRepository
            .loreForCity(cityId)
            .stateIn(
                viewModelScope,
                SharingStarted.WhileSubscribed(5_000),
                null
            )

    fun seedCityLoreIfNeeded(cityId: String) {
        viewModelScope.launch {
            cityLoreRepository.save(
                CityLoreEntity(
                    cityId = cityId,
                    title = "Geschichte der Stadt",
                    text = """
                        Diese Stadt wurde vor Generationen gegründet.

                        Händler, Abenteurer und Machtgruppen haben sie geprägt.
                        Ihre Geschichte ist reich an Intrigen, Umbrüchen und Legenden.
                    """.trimIndent()
                )
            )
        }
    }

    /* ---------------- Stadtviertel (KORREKT) ---------------- */

    fun districtsForCity(cityId: String): StateFlow<List<CityDistrictEntity>> =
        cityDistrictRepository
            .getDistrictsForCity(cityId)   // ✅ exakt dein Interface
            .stateIn(
                viewModelScope,
                SharingStarted.WhileSubscribed(5_000),
                emptyList()
            )

    fun districtById(districtId: String): StateFlow<CityDistrictEntity?> =
        cityDistrictRepository
            .getDistrictById(districtId)   // ✅ exakt dein Interface
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

    /* ---------------- Fraktionen ---------------- */

    fun saveFaction(faction: Faction) {
        factionRepository.save(faction)
    }

    val factions: StateFlow<List<Faction>> =
        factionRepository.factions
            .stateIn(
                viewModelScope,
                SharingStarted.WhileSubscribed(5_000),
                emptyList()
            )
    /* ---------------- POIs ---------------- */

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
    fun visiblePoisForPlayerByCategory(
        categoryId: String
    ): StateFlow<List<PointOfInterest>> =
        poiRepository
            .getVisibleForPlayerByCategory(categoryId)
            .stateIn(
                viewModelScope,
                SharingStarted.WhileSubscribed(5_000),
                emptyList()
            )
    /* ---------------- POIs ---------------- */

    val allPois: StateFlow<List<PointOfInterest>> =
        poiRepository.getAll()
            .stateIn(
                viewModelScope,
                SharingStarted.WhileSubscribed(5_000),
                emptyList()
            )

}
