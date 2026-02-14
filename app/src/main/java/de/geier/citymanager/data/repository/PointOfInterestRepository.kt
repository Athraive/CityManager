package de.geier.citymanager.data.repository

import de.geier.citymanager.data.dao.PointOfInterestDao
import de.geier.citymanager.data.entity.mapper.toEntity
import de.geier.citymanager.data.entity.mapper.toUi
import de.geier.citymanager.ui.AccessContext
import de.geier.citymanager.ui.PointOfInterest
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class PointOfInterestRepository(
    private val cityId: String,
    private val dao: PointOfInterestDao
) {

    fun getPois(accessContext: AccessContext): Flow<List<PointOfInterest>> =
        when {
            accessContext.canEdit() ->
                dao.getAll(cityId).map { list ->
                    list.map { it.toUi() }
                }

            else ->
                dao.getVisibleForPlayer(cityId).map { list ->
                    list.map { it.toUi() }
                }
        }

    fun getPoisByCategory(
        categoryId: String,
        accessContext: AccessContext
    ): Flow<List<PointOfInterest>> =
        when {
            accessContext.canEdit() ->
                dao.getByCategory(cityId, categoryId).map { list ->
                    list.map { it.toUi() }
                }

            else ->
                dao.getVisibleForPlayerByCategory(cityId, categoryId).map { list ->
                    list.map { it.toUi() }
                }
        }

    suspend fun getById(id: String): PointOfInterest? =
        dao.getById(id)?.toUi()

    suspend fun save(
        poi: PointOfInterest,
        accessContext: AccessContext
    ) {
        if (!accessContext.canEdit()) return
        dao.insert(poi.toEntity(cityId))
    }

    suspend fun delete(
        poi: PointOfInterest,
        accessContext: AccessContext
    ) {
        if (!accessContext.canEdit()) return
        dao.delete(poi.toEntity(cityId))
    }
}
