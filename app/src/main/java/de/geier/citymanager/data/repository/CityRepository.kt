package de.geier.citymanager.data.repository

import de.geier.citymanager.data.entity.CityEntity
import kotlinx.coroutines.flow.Flow

interface CityRepository {

    val cities: Flow<List<CityEntity>>

    fun cityById(cityId: String): Flow<CityEntity?>

    suspend fun createCity(city: CityEntity)

    suspend fun deleteCity(cityId: String)
}
