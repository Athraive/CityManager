package de.geier.citymanager.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import de.geier.citymanager.data.entity.CityLoreEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CityLoreDao {

    @Query("SELECT * FROM city_lore WHERE cityId = :cityId LIMIT 1")
    fun loreForCity(cityId: String): Flow<CityLoreEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(lore: CityLoreEntity)

    /**
     * Löscht die Lore einer Stadt.
     * Wird beim Löschen einer Stadt verwendet.
     */
    @Query("DELETE FROM city_lore WHERE cityId = :cityId")
    suspend fun deleteByCity(cityId: String)
}
