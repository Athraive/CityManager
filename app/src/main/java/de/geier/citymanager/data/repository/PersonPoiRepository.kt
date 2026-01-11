package de.geier.citymanager.data.repository

import de.geier.citymanager.data.dao.PersonPoiDao
import de.geier.citymanager.data.entity.PersonPoiCrossRef
import kotlinx.coroutines.flow.Flow

class PersonPoiRepository(
    private val dao: PersonPoiDao
) {

    fun getPoiIdsForPerson(personId: String): Flow<List<String>> {
        return dao.getPoiIdsForPerson(personId)
    }

    suspend fun addPoiToPerson(personId: String, poiId: String) {
        dao.insert(
            PersonPoiCrossRef(
                personId = personId,
                poiId = poiId
            )
        )
    }

    suspend fun removePoiFromPerson(personId: String, poiId: String) {
        dao.delete(personId, poiId)
    }
}
