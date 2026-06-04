package de.geier.citymanager.data.repository

import de.geier.citymanager.data.dao.PersonDao
import de.geier.citymanager.data.entity.mapper.toDomain
import de.geier.citymanager.data.entity.mapper.toEntity
import de.geier.citymanager.ui.AccessContext
import de.geier.citymanager.ui.Person
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class PersonRepository(
    private val cityId: String,
    private val dao: PersonDao
) {

    fun getPersons(accessContext: AccessContext): Flow<List<Person>> {
        return if (accessContext.canEdit()) {
            dao.getAllPersons(cityId)
        } else {
            dao.getVisiblePersons(cityId)
        }.map { list ->
            list.map { it.toDomain() }
        }
    }

    suspend fun getById(id: String): Person? =
        dao.getPersonById(id)?.toDomain()

    suspend fun save(person: Person, accessContext: AccessContext) {
        require(accessContext.canEdit()) {
            "Player is not allowed to save persons"
        }
        dao.upsertPerson(person.toEntity(cityId))
    }
    suspend fun savePlayerNotes(
        personId: String,
        playerNotes: String
    ) {
        dao.updatePlayerNotes(
            personId = personId,
            playerNotes = playerNotes
        )
    }
    suspend fun delete(person: Person, accessContext: AccessContext) {
        require(accessContext.canEdit()) {
            "Player is not allowed to delete persons"
        }
        dao.deletePerson(person.id)
    }
}
