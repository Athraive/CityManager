package de.geier.citymanager.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import de.geier.citymanager.data.entity.PoiCategoryEntity

@Dao
interface PoiCategoryDao {

    @Query("SELECT * FROM poi_categories")
    suspend fun getAll(): List<PoiCategoryEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(category: PoiCategoryEntity)

    @Delete
    suspend fun delete(category: PoiCategoryEntity)
}
