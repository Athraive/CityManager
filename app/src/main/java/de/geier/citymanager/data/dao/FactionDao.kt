package de.geier.citymanager.data.dao

import androidx.room.*
import de.geier.citymanager.data.entity.FactionEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FactionDao {

    @Query("SELECT * FROM factions WHERE cityId = :cityId")
    fun getAll(cityId: String): Flow<List<FactionEntity>>

    @Query("SELECT * FROM factions WHERE id = :id LIMIT 1")
    suspend fun getById(id: String): FactionEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(faction: FactionEntity)

    @Delete
    suspend fun delete(faction: FactionEntity)

    @Query("DELETE FROM factions WHERE cityId = :cityId")
    suspend fun deleteByCity(cityId: String)
}
