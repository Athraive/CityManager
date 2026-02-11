package de.geier.citymanager.data.repository

import de.geier.citymanager.data.dao.PersonFactionDao
import de.geier.citymanager.data.entity.PersonFactionCrossRef
import kotlinx.coroutines.flow.Flow

class PersonFactionRepository(
    private val dao: PersonFactionDao
) {

    fun getFactionIdsForPerson(personId: String): Flow<List<String>> =
        dao.getFactionIdsForPerson(personId)

    /* NEU */
    fun getPersonIdsForFaction(factionId: String): Flow<List<String>> =
        dao.getPersonIdsForFaction(factionId)

    suspend fun addFactionToPerson(personId: String, factionId: String) {
        dao.addFactionToPerson(
            PersonFactionCrossRef(
                personId = personId,
                factionId = factionId
            )
        )
    }

    suspend fun removeFactionFromPerson(personId: String, factionId: String) {
        dao.removeFactionFromPerson(personId, factionId)
    }

    suspend fun removeAllForPerson(personId: String) {
        dao.removeAllForPerson(personId)
    }
}
