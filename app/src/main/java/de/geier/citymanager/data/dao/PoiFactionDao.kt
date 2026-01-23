package de.geier.citymanager.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import de.geier.citymanager.data.entity.PoiFactionCrossRef
import kotlinx.coroutines.flow.Flow

@Dao
interface PoiFactionDao {

    @Query(
        "SELECT factionId FROM poi_faction_cross_ref WHERE poiId = :poiId"
    )
    fun getFactionIdsForPoi(poiId: String): Flow<List<String>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addFactionToPoi(crossRef: PoiFactionCrossRef)

    @Query(
        "DELETE FROM poi_faction_cross_ref WHERE poiId = :poiId AND factionId = :factionId"
    )
    suspend fun removeFactionFromPoi(poiId: String, factionId: String)

    @Query(
        "DELETE FROM poi_faction_cross_ref WHERE poiId = :poiId"
    )
    suspend fun removeAllForPoi(poiId: String)
}
