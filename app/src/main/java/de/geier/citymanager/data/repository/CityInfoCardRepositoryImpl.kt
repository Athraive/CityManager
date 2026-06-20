package de.geier.citymanager.data.repository

import de.geier.citymanager.data.dao.CityInfoCardDao
import de.geier.citymanager.data.entity.CityInfoCardEntity
import kotlinx.coroutines.flow.Flow

class CityInfoCardRepositoryImpl(
    private val dao: CityInfoCardDao
) : CityInfoCardRepository {

    override fun cardsForCity(
        cityId: String
    ): Flow<List<CityInfoCardEntity>> =
        dao.getCardsForCity(cityId)

    override suspend fun saveCard(
        card: CityInfoCardEntity
    ) {
        dao.insert(card)
    }

    override suspend fun deleteCard(
        card: CityInfoCardEntity
    ) {
        dao.delete(card)
    }
}