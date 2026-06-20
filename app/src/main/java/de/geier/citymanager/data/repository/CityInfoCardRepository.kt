package de.geier.citymanager.data.repository

import de.geier.citymanager.data.entity.CityInfoCardEntity
import kotlinx.coroutines.flow.Flow

interface CityInfoCardRepository {

    fun cardsForCity(
        cityId: String
    ): Flow<List<CityInfoCardEntity>>

    suspend fun saveCard(
        card: CityInfoCardEntity
    )

    suspend fun deleteCard(
        card: CityInfoCardEntity
    )
}