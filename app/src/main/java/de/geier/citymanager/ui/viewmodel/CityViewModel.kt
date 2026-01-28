package de.geier.citymanager.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import de.geier.citymanager.data.entity.CityDistrictEntity
import de.geier.citymanager.data.entity.CityLoreEntity
import de.geier.citymanager.data.repository.CityDistrictRepository
import de.geier.citymanager.data.repository.CityLoreRepository
import de.geier.citymanager.data.repository.FactionRepositoryImpl
import de.geier.citymanager.data.repository.PoiCategoryRepository
import de.geier.citymanager.data.repository.PointOfInterestRepository
import de.geier.citymanager.ui.AccessContext
import de.geier.citymanager.ui.Faction
import de.geier.citymanager.ui.PointOfInterest
import de.geier.citymanager.ui.PoiCategory
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class CityViewModel(
    private val accessContext: AccessContext,
    private val poiCategoryRepository: PoiCategoryRepository,
    private val poiRepository: PointOfInterestRepository,
    private val factionRepository: FactionRepositoryImpl,
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

    /* ---------------- Stadtviertel ---------------- */

    fun districtsForCity(cityId: String): StateFlow<List<CityDistrictEntity>> =
        cityDistrictRepository
            .getDistrictsForCity(cityId)
            .stateIn(
                viewModelScope,
                SharingStarted.WhileSubscribed(5_000),
                emptyList()
            )

    fun districtById(districtId: String): StateFlow<CityDistrictEntity?> =
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

    /* ---------------- Fraktionen ---------------- */

    val factions: StateFlow<List<Faction>> =
        factionRepository
            .getAll()
            .stateIn(
                viewModelScope,
                SharingStarted.WhileSubscribed(5_000),
                emptyList()
            )

    fun saveFaction(faction: Faction) {
        viewModelScope.launch {
            factionRepository.save(faction)
        }
    }

    fun deleteFaction(faction: Faction) {
        viewModelScope.launch {
            factionRepository.delete(faction)
        }
    }

    /* ---------------- POI-Kategorien ---------------- */

    val poiCategories: StateFlow<List<PoiCategory>> =
        poiCategoryRepository
            .categories
            .stateIn(
                viewModelScope,
                SharingStarted.WhileSubscribed(5_000),
                emptyList()
            )

    /* ---------------- POIs (rollenbewusst) ---------------- */

    val allPois: StateFlow<List<PointOfInterest>> =
        poiRepository
            .getPois(accessContext)
            .stateIn(
                viewModelScope,
                SharingStarted.WhileSubscribed(5_000),
                emptyList()
            )

    fun poisByCategory(categoryId: String): StateFlow<List<PointOfInterest>> =
        poiRepository
            .getPoisByCategory(categoryId, accessContext)
            .stateIn(
                viewModelScope,
                SharingStarted.WhileSubscribed(5_000),
                emptyList()
            )

    /* ---------------- POI-CRUD (SL) ---------------- */

    fun savePoi(poi: PointOfInterest) {
        viewModelScope.launch {
            poiRepository.save(poi, accessContext)
        }
    }

    fun deletePoi(poi: PointOfInterest) {
        viewModelScope.launch {
            poiRepository.delete(poi, accessContext)
        }
    }
}
