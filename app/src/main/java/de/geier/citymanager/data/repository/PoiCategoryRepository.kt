package de.geier.citymanager.data.repository

import de.geier.citymanager.data.dao.PoiCategoryDao
import de.geier.citymanager.data.entity.mapper.toDomain
import de.geier.citymanager.data.entity.mapper.toEntity
import de.geier.citymanager.ui.PoiCategory
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class PoiCategoryRepository(
    private val cityId: String,
    private val dao: PoiCategoryDao
) {

    val categories: Flow<List<PoiCategory>> =
        dao.getAll(cityId).map { list ->
            list.map { it.toDomain() }
        }

    suspend fun getById(id: String): PoiCategory? =
        dao.getById(id)?.toDomain()

    suspend fun save(category: PoiCategory) {
        dao.insert(category.toEntity(cityId))
    }

    suspend fun delete(category: PoiCategory) {
        dao.delete(category.toEntity(cityId))
    }
}
