package de.geier.citymanager.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import de.geier.citymanager.data.entity.CityDistrictEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CityDistrictDao {

    @Query("""
        SELECT * FROM city_districts
        WHERE cityId = :cityId
        ORDER BY orderIndex ASC
    """)
    fun getDistrictsForCity(cityId: String): Flow<List<CityDistrictEntity>>

    @Query("""
        SELECT * FROM city_districts
        WHERE id = :districtId
        LIMIT 1
    """)
    fun getDistrictById(districtId: String): Flow<CityDistrictEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun save(district: CityDistrictEntity)

    @Query("DELETE FROM city_districts WHERE id = :districtId")
    suspend fun delete(districtId: String)
}
