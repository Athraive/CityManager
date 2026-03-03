package de.geier.citymanager.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import de.geier.citymanager.data.entity.PersonFactionCrossRef
import kotlinx.coroutines.flow.Flow

@Dao
interface PersonFactionDao {

    /* ---------- Person → Fraktionen ---------- */

    @Query(
        """
        SELECT factionId 
        FROM person_faction_cross_ref 
        WHERE personId = :personId
        """
    )
    fun getFactionIdsForPerson(personId: String): Flow<List<String>>

    /* ---------- Fraktion → Personen ---------- */

    @Query(
        """
        SELECT personId 
        FROM person_faction_cross_ref 
        WHERE factionId = :factionId
        """
    )
    fun getPersonIdsForFaction(factionId: String): Flow<List<String>>

    /* ---------- NEU: Alle CrossRefs ---------- */

    @Query(
        """
        SELECT * 
        FROM person_faction_cross_ref
        """
    )
    fun getAll(): Flow<List<PersonFactionCrossRef>>

    /* ---------- Mutationen ---------- */

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addFactionToPerson(crossRef: PersonFactionCrossRef)

    @Query(
        """
        DELETE FROM person_faction_cross_ref
        WHERE personId = :personId AND factionId = :factionId
        """
    )
    suspend fun removeFactionFromPerson(personId: String, factionId: String)

    @Query(
        """
        DELETE FROM person_faction_cross_ref
        WHERE personId = :personId
        """
    )
    suspend fun removeAllForPerson(personId: String)
}