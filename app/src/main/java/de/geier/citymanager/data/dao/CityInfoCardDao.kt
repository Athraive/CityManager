package de.geier.citymanager.data.dao

import androidx.room.*
import de.geier.citymanager.data.entity.CityInfoCardEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CityInfoCardDao {

    @Query("""
        SELECT *
        FROM city_info_cards
        WHERE cityId = :cityId
        ORDER BY `order`
    """)
    fun getCardsForCity(
        cityId: String
    ): Flow<List<CityInfoCardEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(
        card: CityInfoCardEntity
    )

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(
        cards: List<CityInfoCardEntity>
    )

    @Delete
    suspend fun delete(
        card: CityInfoCardEntity
    )

    @Update
    suspend fun update(
        card: CityInfoCardEntity
    )
}