package de.geier.citymanager.data.repository

import de.geier.citymanager.data.dao.PointOfInterestDao
import de.geier.citymanager.data.entity.mapper.toDomain
import de.geier.citymanager.data.entity.mapper.toEntity
import de.geier.citymanager.ui.AccessContext
import de.geier.citymanager.ui.PointOfInterest
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class PointOfInterestRepository(
    private val dao: PointOfInterestDao
) {

    /* ---------------- Lesen ---------------- */

    fun getPois(accessContext: AccessContext): Flow<List<PointOfInterest>> {
        val source = if (accessContext.canEdit()) {
            dao.getAll()
        } else {
            dao.getVisibleForPlayer()
        }

        return source.map { list ->
            list.map { it.toDomain() }
        }
    }

    fun getPoisByCategory(
        categoryId: String,
        accessContext: AccessContext
    ): Flow<List<PointOfInterest>> {
        val source = if (accessContext.canEdit()) {
            dao.getByCategory(categoryId)
        } else {
            dao.getVisibleForPlayerByCategory(categoryId)
        }

        return source.map { list ->
            list.map { it.toDomain() }
        }
    }

    /* ---------------- Einzel ---------------- */

    suspend fun getById(id: String): PointOfInterest? =
        dao.getById(id)?.toDomain()

    /* ---------------- Mutationen (SL) ---------------- */

    suspend fun save(poi: PointOfInterest, accessContext: AccessContext) {
        require(accessContext.canEdit()) {
            "Player is not allowed to save POIs"
        }
        dao.insert(poi.toEntity())
    }

    suspend fun delete(poi: PointOfInterest, accessContext: AccessContext) {
        require(accessContext.canEdit()) {
            "Player is not allowed to delete POIs"
        }
        dao.delete(poi.toEntity())
    }
}
