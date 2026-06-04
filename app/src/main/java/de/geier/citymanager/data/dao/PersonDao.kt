package de.geier.citymanager.data.dao

import androidx.room.*
import de.geier.citymanager.data.entity.PersonEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PersonDao {

    @Query("SELECT * FROM persons WHERE cityId = :cityId")
    fun getAllPersons(cityId: String): Flow<List<PersonEntity>>

    @Query("SELECT * FROM persons WHERE cityId = :cityId AND visible = 1")
    fun getVisiblePersons(cityId: String): Flow<List<PersonEntity>>

    @Query("SELECT * FROM persons WHERE id = :personId LIMIT 1")
    suspend fun getPersonById(personId: String): PersonEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertPerson(person: PersonEntity)

    @Query("""
    UPDATE persons
    SET playerNotes = :playerNotes
    WHERE id = :personId
""")
    suspend fun updatePlayerNotes(
        personId: String,
        playerNotes: String
    )
    @Query("DELETE FROM persons WHERE id = :personId")
    suspend fun deletePerson(personId: String)

    @Query("DELETE FROM persons WHERE cityId = :cityId")
    suspend fun deleteByCity(cityId: String)
}
