package de.geier.citymanager.data.dao

import androidx.room.*
import de.geier.citymanager.data.entity.PoiCategoryEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PoiCategoryDao {

    @Query("SELECT * FROM poi_categories WHERE cityId = :cityId")
    fun getAll(cityId: String): Flow<List<PoiCategoryEntity>>

    @Query("SELECT * FROM poi_categories WHERE id = :id LIMIT 1")
    suspend fun getById(id: String): PoiCategoryEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(category: PoiCategoryEntity)

    @Delete
    suspend fun delete(category: PoiCategoryEntity)

    @Query("DELETE FROM poi_categories WHERE cityId = :cityId")
    suspend fun deleteByCity(cityId: String)
}
