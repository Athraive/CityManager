package de.geier.citymanager.data.repository

import de.geier.citymanager.data.dao.PointOfInterestDao
import de.geier.citymanager.data.entity.mapper.toDomain
import de.geier.citymanager.data.entity.mapper.toEntity
import de.geier.citymanager.ui.PointOfInterest
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class PointOfInterestRepository(
    private val dao: PointOfInterestDao
) {

    fun getAll(): Flow<List<PointOfInterest>> =
        dao.getAll().map { list ->
            list.map { it.toDomain() }
        }

    fun getByCategory(categoryId: String): Flow<List<PointOfInterest>> =
        dao.getByCategory(categoryId).map { list ->
            list.map { it.toDomain() }
        }

    suspend fun getById(id: String): PointOfInterest? =
        dao.getById(id)?.toDomain()

    suspend fun save(poi: PointOfInterest) {
        dao.insert(poi.toEntity())
    }

    suspend fun delete(poi: PointOfInterest) {
        dao.delete(poi.toEntity())
    }
}
