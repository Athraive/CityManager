package de.geier.citymanager.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import de.geier.citymanager.data.entity.PointOfInterestEntity

@Dao
interface PointOfInterestDao {

    @Query("SELECT * FROM points_of_interest")
    suspend fun getAll(): List<PointOfInterestEntity>

    @Query("SELECT * FROM points_of_interest WHERE categoryId = :categoryId")
    suspend fun getByCategory(categoryId: String): List<PointOfInterestEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(poi: PointOfInterestEntity)

    @Delete
    suspend fun delete(poi: PointOfInterestEntity)
}
