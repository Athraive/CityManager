package de.geier.citymanager.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import de.geier.citymanager.data.entity.CityDistrictEntity
import de.geier.citymanager.data.entity.CityEntity
import de.geier.citymanager.data.entity.CityLoreEntity
import de.geier.citymanager.data.repository.*
import de.geier.citymanager.ui.AccessContext
import de.geier.citymanager.ui.Faction
import de.geier.citymanager.ui.PointOfInterest
import de.geier.citymanager.ui.PoiCategory
import de.geier.citymanager.ui.Person
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class CityViewModel(
    private val accessContext: AccessContext,
    private val cityRepository: CityRepository,
    private val poiCategoryRepository: PoiCategoryRepository,
    private val poiRepository: PointOfInterestRepository,
    private val factionRepository: FactionRepositoryImpl,
    private val cityDistrictRepository: CityDistrictRepository,
    private val cityLoreRepository: CityLoreRepository,
    private val personRepository: PersonRepository
) : ViewModel() {

    /* =====================================================
     * Aktuelle Stadt
     * ===================================================== */

    val city: StateFlow<CityEntity?> =
        cityRepository
            .cityById(accessContext.cityId)
            .stateIn(
                viewModelScope,
                SharingStarted.WhileSubscribed(5_000),
                null
            )

    /* =====================================================
     * Stadtgeschichte & Metadaten
     * ===================================================== */

    fun cityLore(cityId: String): StateFlow<CityLoreEntity?> =
        cityLoreRepository
            .loreForCity(cityId)
            .stateIn(
                viewModelScope,
                SharingStarted.WhileSubscribed(5_000),
                null
            )

    fun saveCityMapUri(
        cityId: String,
        uri: String
    ) {
        viewModelScope.launch {
            val existing =
                cityLoreRepository.loreForCity(cityId).first()

            cityLoreRepository.save(
                (existing ?: CityLoreEntity(
                    cityId = cityId,
                    title = "Über die Stadt",
                    text = ""
                )).copy(
                    mapImageUri = uri
                )
            )
        }
    }

    /* =====================================================
     * Stadtviertel
     * ===================================================== */

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

    /* =====================================================
     * Fraktionen
     * ===================================================== */

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

    /* =====================================================
     * POI-Kategorien
     * ===================================================== */

    val poiCategories: StateFlow<List<PoiCategory>> =
        poiCategoryRepository
            .categories
            .stateIn(
                viewModelScope,
                SharingStarted.WhileSubscribed(5_000),
                emptyList()
            )

    /* =====================================================
     * POIs
     * ===================================================== */

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

    /* =====================================================
     * Personen
     * ===================================================== */

    val allPersons: StateFlow<List<Person>> =
        personRepository
            .getPersons(accessContext)
            .stateIn(
                viewModelScope,
                SharingStarted.WhileSubscribed(5_000),
                emptyList()
            )
}
