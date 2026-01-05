package de.geier.citymanager.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import de.geier.citymanager.data.entity.PointOfInterestEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PointOfInterestDao {

    @Query("SELECT * FROM pois")
    fun getAll(): Flow<List<PointOfInterestEntity>>

    @Query("SELECT * FROM pois WHERE categoryId = :categoryId")
    fun getByCategory(categoryId: String): Flow<List<PointOfInterestEntity>>

    @Query("SELECT * FROM pois WHERE id = :id LIMIT 1")
    suspend fun getById(id: String): PointOfInterestEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(poi: PointOfInterestEntity)

    @Delete
    suspend fun delete(poi: PointOfInterestEntity)
}
