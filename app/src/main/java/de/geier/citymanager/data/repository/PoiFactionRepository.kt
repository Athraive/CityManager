package de.geier.citymanager.data.repository

import de.geier.citymanager.data.dao.PoiFactionDao
import de.geier.citymanager.data.entity.PoiFactionCrossRef
import kotlinx.coroutines.flow.Flow

class PoiFactionRepository(
    private val dao: PoiFactionDao
) {

    fun getFactionIdsForPoi(poiId: String): Flow<List<String>> =
        dao.getFactionIdsForPoi(poiId)

    /* NEU */
    fun getPoiIdsForFaction(factionId: String): Flow<List<String>> =
        dao.getPoiIdsForFaction(factionId)

    suspend fun addFactionToPoi(poiId: String, factionId: String) {
        dao.addFactionToPoi(
            PoiFactionCrossRef(
                poiId = poiId,
                factionId = factionId
            )
        )
    }

    suspend fun removeFactionFromPoi(poiId: String, factionId: String) {
        dao.removeFactionFromPoi(poiId, factionId)
    }

    suspend fun removeAllForPoi(poiId: String) {
        dao.removeAllForPoi(poiId)
    }
}
