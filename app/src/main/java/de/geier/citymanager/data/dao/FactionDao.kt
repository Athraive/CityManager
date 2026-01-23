package de.geier.citymanager.data.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import de.geier.citymanager.data.entity.FactionEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FactionDao {

    @Query("SELECT * FROM factions")
    fun getAll(): Flow<List<FactionEntity>>

    @Query("SELECT * FROM factions WHERE id = :id")
    fun getById(id: String): Flow<FactionEntity?>

    @Upsert
    suspend fun save(faction: FactionEntity)

    @Query("DELETE FROM factions WHERE id = :id")
    suspend fun delete(id: String)
}
