package de.geier.citymanager.data.repository

import de.geier.citymanager.data.dao.PoiFactionDao
import de.geier.citymanager.data.entity.PoiFactionCrossRef
import kotlinx.coroutines.flow.Flow

class PoiFactionRepository(
    private val dao: PoiFactionDao
) {

    /* ---------- POI → Fraktionen ---------- */

    fun getFactionIdsForPoi(poiId: String): Flow<List<String>> {
        return dao.getFactionIdsForPoi(poiId)
    }

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
