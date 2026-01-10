package de.geier.citymanager.data.repository

import de.geier.citymanager.data.dao.PersonDao
import de.geier.citymanager.data.entity.mapper.toDomain
import de.geier.citymanager.data.entity.mapper.toEntity
import de.geier.citymanager.ui.Person
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class PersonRepository(
    private val dao: PersonDao
) {

    fun getAll(): Flow<List<Person>> =
        dao.getAllPersons().map { list ->
            list.map { it.toDomain() }
        }

    fun getVisible(): Flow<List<Person>> =
        dao.getVisiblePersons().map { list ->
            list.map { it.toDomain() }
        }

    suspend fun getById(id: String): Person? =
        dao.getPersonById(id)?.toDomain()

    suspend fun save(person: Person) {
        dao.upsertPerson(person.toEntity())
    }

    suspend fun delete(person: Person) {
        dao.deletePerson(person.id)
    }
}
