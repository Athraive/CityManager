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
import de.geier.citymanager.data.entity.CityInfoCardEntity

class CityViewModel(
    private val accessContext: AccessContext,
    private val cityRepository: CityRepository,
    private val cityInfoCardRepository: CityInfoCardRepository,
    private val poiCategoryRepository: PoiCategoryRepository,
    private val poiRepository: PointOfInterestRepository,
    private val factionRepository: FactionRepositoryImpl,
    private val cityDistrictRepository: CityDistrictRepository,
    private val cityLoreRepository: CityLoreRepository,
    private val personRepository: PersonRepository
) : ViewModel() {

    val city: StateFlow<CityEntity?> =
        cityRepository
            .cityById(accessContext.cityId)
            .stateIn(
                viewModelScope,
                SharingStarted.WhileSubscribed(5_000),
                null
            )

    val cityInfoCards =
        cityInfoCardRepository
            .cardsForCity(accessContext.cityId)
            .stateIn(
                viewModelScope,
                SharingStarted.WhileSubscribed(5_000),
                emptyList()
            )

    val cityLore: StateFlow<CityLoreEntity?> =
        cityLoreRepository
            .loreForCity(accessContext.cityId)
            .stateIn(
                viewModelScope,
                SharingStarted.Eagerly,
                null
            )

    fun saveCityMapUri(
        cityId: String,
        uri: String
    ) {
        viewModelScope.launch {

            val existing = cityLore.value

            val updated = (existing ?: CityLoreEntity(
                cityId = accessContext.cityId,
                title = "Über die Stadt",
                text = ""
            )).copy(
                mapImageUri = uri
            )

            cityLoreRepository.save(updated)
        }
    }

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

    val poiCategories: StateFlow<List<PoiCategory>> =
        poiCategoryRepository
            .categories
            .stateIn(
                viewModelScope,
                SharingStarted.WhileSubscribed(5_000),
                emptyList()
            )

    val allPois: StateFlow<List<PointOfInterest>> =
        poiRepository
            .getPois(accessContext)
            .stateIn(
                viewModelScope,
                SharingStarted.WhileSubscribed(5_000),
                emptyList()
            )

    private val poiCategoryFlows =
        mutableMapOf<String, StateFlow<List<PointOfInterest>>>()

    fun poisByCategory(
        categoryId: String
    ): StateFlow<List<PointOfInterest>> {

        return poiCategoryFlows.getOrPut(categoryId) {

            poiRepository
                .getPoisByCategory(categoryId, accessContext)
                .stateIn(
                    viewModelScope,
                    SharingStarted.WhileSubscribed(5_000),
                    emptyList()
                )
        }
    }

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

    // ✅ Nullable Koordinaten
    fun updatePoiCoordinates(
        poiId: String,
        mapX: Float?,
        mapY: Float?
    ) {
        if (!accessContext.canEdit()) return

        viewModelScope.launch {
            val existing =
                poiRepository.getById(poiId) ?: return@launch

            val updated = existing.copy(
                mapX = mapX,
                mapY = mapY
            )

            poiRepository.save(updated, accessContext)
        }
    }

    val allPersons: StateFlow<List<Person>> =
        personRepository
            .getPersons(accessContext)
            .stateIn(
                viewModelScope,
                SharingStarted.WhileSubscribed(5_000),
                emptyList()
            )

    // ✅ Nullable Koordinaten
    fun updatePersonCoordinates(
        personId: String,
        mapX: Float?,
        mapY: Float?
    ) {
        if (!accessContext.canEdit()) return

        viewModelScope.launch {
            val existing =
                personRepository.getById(personId)
                    ?: return@launch

            val updated = existing.copy(
                mapX = mapX,
                mapY = mapY
            )

            personRepository.save(updated, accessContext)
        }
    }
    fun saveCity(updated: CityEntity) {
        if (!accessContext.canEdit()) return

        viewModelScope.launch {
            cityRepository.save(updated)
        }
    }

    fun saveCityLore(lore: CityLoreEntity) {
        if (!accessContext.canEdit()) return

        viewModelScope.launch {
            cityLoreRepository.save(lore)
        }
    }

    fun createCard(
        title: String
    ) {

        viewModelScope.launch {

            val isStandardTemplate =
                title != "Neue Karte"

            if (isStandardTemplate) {

                val alreadyExists =
                    cityInfoCards.value.any {
                        it.title.trim().equals(
                            title.trim(),
                            ignoreCase = true
                        )
                    }

                if (alreadyExists) {
                    return@launch
                }
            }

            val nextOrder =
                cityInfoCards.value.size

            cityInfoCardRepository.saveCard(
                CityInfoCardEntity(
                    id = java.util.UUID.randomUUID().toString(),
                    cityId = accessContext.cityId,
                    title = title,
                    content = "",
                    visible = true,
                    order = nextOrder
                )
            )
        }
    }

    fun saveCard(
        card: CityInfoCardEntity
    ) {
        viewModelScope.launch {
            cityInfoCardRepository.saveCard(card)
        }
    }

    fun deleteCard(
        card: CityInfoCardEntity
    ) {
        viewModelScope.launch {
            cityInfoCardRepository.deleteCard(card)
        }
    }
}