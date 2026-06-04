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

    @Query("SELECT * FROM pois WHERE cityId = :cityId")
    fun getAll(cityId: String): Flow<List<PointOfInterestEntity>>

    @Query("SELECT * FROM pois WHERE cityId = :cityId AND categoryId = :categoryId")
    fun getByCategory(
        cityId: String,
        categoryId: String
    ): Flow<List<PointOfInterestEntity>>

    /**
     * Alle für Spieler sichtbaren POIs.
     *
     * Wichtig:
     * Die Sichtbarkeit der Kategorie spielt hier bewusst
     * keine Rolle mehr.
     */
    @Query(
        """
        SELECT *
        FROM pois
        WHERE cityId = :cityId
          AND visible = 1
        """
    )
    fun getVisiblePois(
        cityId: String
    ): Flow<List<PointOfInterestEntity>>

    @Query(
        """
        SELECT pois.* 
        FROM pois
        INNER JOIN poi_categories 
            ON pois.categoryId = poi_categories.id
        WHERE pois.cityId = :cityId
          AND poi_categories.visible = 1
          AND pois.visible = 1
        """
    )
    fun getVisibleForPlayer(
        cityId: String
    ): Flow<List<PointOfInterestEntity>>

    @Query(
        """
        SELECT pois.* 
        FROM pois
        INNER JOIN poi_categories 
            ON pois.categoryId = poi_categories.id
        WHERE pois.cityId = :cityId
          AND poi_categories.visible = 1
          AND pois.visible = 1
          AND pois.categoryId = :categoryId
        """
    )
    fun getVisibleForPlayerByCategory(
        cityId: String,
        categoryId: String
    ): Flow<List<PointOfInterestEntity>>

    @Query("SELECT * FROM pois WHERE id = :id LIMIT 1")
    suspend fun getById(id: String): PointOfInterestEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(poi: PointOfInterestEntity)

    @Delete
    suspend fun delete(poi: PointOfInterestEntity)

    @Query("DELETE FROM pois WHERE cityId = :cityId")
    suspend fun deleteByCity(cityId: String)
}