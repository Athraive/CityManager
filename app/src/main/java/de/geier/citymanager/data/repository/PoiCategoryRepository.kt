package de.geier.citymanager.data.repository

import de.geier.citymanager.data.dao.PoiCategoryDao
import de.geier.citymanager.data.mapper.toEntity
import de.geier.citymanager.data.mapper.toModel
import de.geier.citymanager.ui.PoiCategory

class PoiCategoryRepository(
    private val dao: PoiCategoryDao
) {

    suspend fun getAll(): List<PoiCategory> =
        dao.getAll().map { it.toModel() }

    suspend fun save(category: PoiCategory) {
        dao.insert(category.toEntity())
    }

    suspend fun delete(category: PoiCategory) {
        dao.delete(category.toEntity())
    }
}
