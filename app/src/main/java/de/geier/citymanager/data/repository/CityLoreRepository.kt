package de.geier.citymanager.data.repository

import de.geier.citymanager.data.dao.CityLoreDao
import de.geier.citymanager.data.entity.CityLoreEntity
import kotlinx.coroutines.flow.Flow

class CityLoreRepository(
    private val dao: CityLoreDao
) {
    fun loreForCity(cityId: String): Flow<CityLoreEntity?> =
        dao.loreForCity(cityId)

    suspend fun save(lore: CityLoreEntity) {
        dao.upsert(lore)
    }
}
