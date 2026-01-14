package de.geier.citymanager.data.repository

import de.geier.citymanager.data.dao.CityDistrictDao
import de.geier.citymanager.data.entity.CityDistrictEntity
import kotlinx.coroutines.flow.Flow

class CityDistrictRepositoryImpl(
    private val dao: CityDistrictDao
) : CityDistrictRepository {

    override fun getDistrictsForCity(cityId: String): Flow<List<CityDistrictEntity>> =
        dao.getDistrictsForCity(cityId)

    override fun getDistrictById(districtId: String): Flow<CityDistrictEntity?> =
        dao.getDistrictById(districtId)

    override suspend fun save(district: CityDistrictEntity) {
        dao.save(district)
    }

    override suspend fun delete(districtId: String) {
        dao.delete(districtId)
    }
}
