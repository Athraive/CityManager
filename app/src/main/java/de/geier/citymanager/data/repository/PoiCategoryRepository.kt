package de.geier.citymanager.data.repository

import de.geier.citymanager.data.dao.PoiCategoryDao
import de.geier.citymanager.data.entity.PoiCategoryEntity
import de.geier.citymanager.data.entity.mapper.toDomain
import de.geier.citymanager.data.entity.mapper.toEntity
import de.geier.citymanager.ui.PoiCategory
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class PoiCategoryRepository(
    private val dao: PoiCategoryDao
) {

    /** Alle Kategorien als Flow */
    val categories: Flow<List<PoiCategory>> =
        dao.getAll().map { list ->
            list.map { it.toDomain() }
        }

    /** Einzelne Kategorie laden */
    suspend fun getById(id: String): PoiCategory? {
        return dao.getById(id)?.toDomain()
    }

    /** Kategorie speichern (neu ODER bearbeiten) */
    suspend fun save(category: PoiCategory) {
        dao.insert(category.toEntity())
    }

    /** Kategorie löschen */
    suspend fun delete(category: PoiCategory) {
        dao.delete(category.toEntity())
    }
}
