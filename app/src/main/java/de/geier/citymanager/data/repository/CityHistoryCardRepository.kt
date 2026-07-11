package de.geier.citymanager.data.repository

import de.geier.citymanager.data.entity.CityHistoryCardEntity
import kotlinx.coroutines.flow.Flow

interface CityHistoryCardRepository {

    fun cardsForCity(
        cityId: String
    ): Flow<List<CityHistoryCardEntity>>

    suspend fun saveCard(
        card: CityHistoryCardEntity
    )

    suspend fun deleteCard(
        card: CityHistoryCardEntity
    )
}