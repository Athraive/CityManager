package de.geier.citymanager.data.repository

import de.geier.citymanager.data.dao.CityDao
import de.geier.citymanager.data.dao.CityDistrictDao
import de.geier.citymanager.data.dao.CityLoreDao
import de.geier.citymanager.data.dao.FactionDao
import de.geier.citymanager.data.dao.PersonDao
import de.geier.citymanager.data.dao.PointOfInterestDao
import de.geier.citymanager.data.dao.PoiCategoryDao
import de.geier.citymanager.data.entity.CityEntity
import kotlinx.coroutines.flow.Flow

/**
 * Verantwortlich für Verwaltung von Städten.
 *
 * Beim Löschen einer Stadt werden alle zugehörigen Daten
 * explizit mitgelöscht (manuelles Cascade).
 */
class CityRepositoryImpl(
    private val cityDao: CityDao,
    private val personDao: PersonDao,
    private val poiDao: PointOfInterestDao,
    private val factionDao: FactionDao,
    private val poiCategoryDao: PoiCategoryDao,
    private val districtDao: CityDistrictDao,
    private val loreDao: CityLoreDao
) : CityRepository {

    override val cities: Flow<List<CityEntity>> =
        cityDao.getAllCities()

    override fun cityById(cityId: String): Flow<CityEntity?> =
        cityDao.getCityById(cityId)

    override suspend fun createCity(city: CityEntity) {
        cityDao.insert(city)
    }

    /**
     * 🔹 NEU: Update / Save bestehender Städte
     */
    override suspend fun save(city: CityEntity) {
        cityDao.insert(city) // REPLACE → Update
    }

    override suspend fun deleteCity(cityId: String) {

        // 🔹 Manuelles Cascade – bewusst explizit
        personDao.deleteByCity(cityId)
        poiDao.deleteByCity(cityId)
        factionDao.deleteByCity(cityId)
        poiCategoryDao.deleteByCity(cityId)
        districtDao.deleteByCity(cityId)
        loreDao.deleteByCity(cityId)

        cityDao.deleteCityById(cityId)
    }
}