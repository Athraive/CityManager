package de.geier.citymanager.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import de.geier.citymanager.data.entity.PersonPoiCrossRef
import kotlinx.coroutines.flow.Flow

@Dao
interface PersonPoiDao {

    /* ---------- Person → POIs ---------- */

    @Query(
        """
        SELECT poiId 
        FROM person_poi_cross_ref 
        WHERE personId = :personId
        """
    )
    fun getPoiIdsForPerson(personId: String): Flow<List<String>>

    /* ---------- POI → Personen (NEU) ---------- */

    @Query(
        """
        SELECT personId 
        FROM person_poi_cross_ref 
        WHERE poiId = :poiId
        """
    )
    fun getPersonIdsForPoi(poiId: String): Flow<List<String>>

    /* ---------- Mutationen ---------- */

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(crossRef: PersonPoiCrossRef)

    @Query(
        """
        DELETE FROM person_poi_cross_ref
        WHERE personId = :personId AND poiId = :poiId
        """
    )
    suspend fun delete(personId: String, poiId: String)
}
