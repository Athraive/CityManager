package de.geier.citymanager.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import de.geier.citymanager.data.entity.CityEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CityDao {

    @Query("SELECT * FROM cities ORDER BY createdAt ASC")
    fun getAllCities(): Flow<List<CityEntity>>

    @Query("SELECT * FROM cities WHERE id = :cityId LIMIT 1")
    fun getCityById(cityId: String): Flow<CityEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(city: CityEntity)

    @Query("DELETE FROM cities WHERE id = :cityId")
    suspend fun deleteCityById(cityId: String)
}
