package de.geier.citymanager.data.repository

import de.geier.citymanager.data.dao.CityHistoryCardDao
import de.geier.citymanager.data.entity.CityHistoryCardEntity
import kotlinx.coroutines.flow.Flow

class CityHistoryCardRepositoryImpl(
    private val dao: CityHistoryCardDao
) : CityHistoryCardRepository {

    override fun cardsForCity(
        cityId: String
    ): Flow<List<CityHistoryCardEntity>> =
        dao.getCardsForCity(cityId)

    override suspend fun saveCard(
        card: CityHistoryCardEntity
    ) {
        dao.insert(card)
    }

    override suspend fun deleteCard(
        card: CityHistoryCardEntity
    ) {
        dao.delete(card)
    }
}