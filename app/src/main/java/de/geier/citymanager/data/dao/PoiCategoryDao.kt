package de.geier.citymanager.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import de.geier.citymanager.data.entity.PoiCategoryEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PoiCategoryDao {

    @Query("SELECT * FROM poi_categories")
    fun getAll(): Flow<List<PoiCategoryEntity>>

    @Query("SELECT * FROM poi_categories WHERE id = :id LIMIT 1")
    suspend fun getById(id: String): PoiCategoryEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(category: PoiCategoryEntity)

    @Delete
    suspend fun delete(category: PoiCategoryEntity)
}
