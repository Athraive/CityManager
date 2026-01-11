package de.geier.citymanager.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import de.geier.citymanager.data.entity.PersonPoiCrossRef
import kotlinx.coroutines.flow.Flow

@Dao
interface PersonPoiDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(ref: PersonPoiCrossRef)

    @Query(
        "DELETE FROM person_poi_cross_ref " +
                "WHERE personId = :personId AND poiId = :poiId"
    )
    suspend fun delete(personId: String, poiId: String)

    @Query(
        "SELECT poiId FROM person_poi_cross_ref " +
                "WHERE personId = :personId"
    )
    fun getPoiIdsForPerson(personId: String): Flow<List<String>>
}
