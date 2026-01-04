package de.geier.citymanager.data.repository

import de.geier.citymanager.data.dao.PointOfInterestDao
import de.geier.citymanager.data.mapper.toEntity
import de.geier.citymanager.data.mapper.toModel
import de.geier.citymanager.ui.PointOfInterest

class PointOfInterestRepository(
    private val dao: PointOfInterestDao
) {

    suspend fun getAll(): List<PointOfInterest> =
        dao.getAll().map { it.toModel() }

    suspend fun getByCategory(categoryId: String): List<PointOfInterest> =
        dao.getByCategory(categoryId).map { it.toModel() }

    suspend fun save(poi: PointOfInterest) {
        dao.insert(poi.toEntity())
    }

    suspend fun delete(poi: PointOfInterest) {
        dao.delete(poi.toEntity())
    }
}
