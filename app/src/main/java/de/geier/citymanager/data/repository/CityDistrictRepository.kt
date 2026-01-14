package de.geier.citymanager.data.repository

import de.geier.citymanager.data.entity.CityDistrictEntity
import kotlinx.coroutines.flow.Flow

interface CityDistrictRepository {

    fun getDistrictsForCity(cityId: String): Flow<List<CityDistrictEntity>>

    fun getDistrictById(districtId: String): Flow<CityDistrictEntity?>

    suspend fun save(district: CityDistrictEntity)

    suspend fun delete(districtId: String)
}
