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

    /* ---------------- SL ---------------- */

    @Query("SELECT * FROM pois")
    fun getAll(): Flow<List<PointOfInterestEntity>>

    @Query("SELECT * FROM pois WHERE categoryId = :categoryId")
    fun getByCategory(categoryId: String): Flow<List<PointOfInterestEntity>>

    /* ---------------- Spieler (gehärtet) ---------------- */

    @Query(
        """
        SELECT pois.* 
        FROM pois
        INNER JOIN poi_categories 
            ON pois.categoryId = poi_categories.id
        WHERE poi_categories.visible = 1
          AND pois.visible = 1
        """
    )
    fun getVisibleForPlayer(): Flow<List<PointOfInterestEntity>>

    @Query(
        """
        SELECT pois.* 
        FROM pois
        INNER JOIN poi_categories 
            ON pois.categoryId = poi_categories.id
        WHERE poi_categories.visible = 1
          AND pois.visible = 1
          AND pois.categoryId = :categoryId
        """
    )
    fun getVisibleForPlayerByCategory(categoryId: String): Flow<List<PointOfInterestEntity>>

    /* ---------------- Einzelabfrage ---------------- */

    @Query("SELECT * FROM pois WHERE id = :id LIMIT 1")
    suspend fun getById(id: String): PointOfInterestEntity?

    /* ---------------- Mutationen ---------------- */

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(poi: PointOfInterestEntity)

    @Delete
    suspend fun delete(poi: PointOfInterestEntity)
}
