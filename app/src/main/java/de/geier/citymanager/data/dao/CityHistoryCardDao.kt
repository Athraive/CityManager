package de.geier.citymanager.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import de.geier.citymanager.data.entity.CityHistoryCardEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CityHistoryCardDao {

    @Query("""
        SELECT *
        FROM city_history_cards
        WHERE cityId = :cityId
        ORDER BY `order`
    """)
    fun getCardsForCity(
        cityId: String
    ): Flow<List<CityHistoryCardEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(
        card: CityHistoryCardEntity
    )

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(
        cards: List<CityHistoryCardEntity>
    )

    @Delete
    suspend fun delete(
        card: CityHistoryCardEntity
    )

    @Update
    suspend fun update(
        card: CityHistoryCardEntity
    )
}