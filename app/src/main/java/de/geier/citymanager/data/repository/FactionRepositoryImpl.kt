package de.geier.citymanager.data.repository

import de.geier.citymanager.data.dao.FactionDao
import de.geier.citymanager.data.entity.FactionEntity
import de.geier.citymanager.ui.Faction
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class FactionRepositoryImpl(
    private val cityId: String,
    private val dao: FactionDao
) {

    fun getAll(): Flow<List<Faction>> =
        dao.getAll(cityId).map { entities ->
            entities.map { it.toDomain() }
        }

    suspend fun save(faction: Faction) {
        dao.insert(faction.toEntity(cityId))
    }

    suspend fun delete(faction: Faction) {
        dao.delete(faction.toEntity(cityId))
    }

    suspend fun getById(id: String): Faction? =
        dao.getById(id)?.toDomain()
}

/* ---------- Mapper ---------- */

private fun FactionEntity.toDomain() =
    Faction(
        id = id,
        name = name,
        description = description,
        playerNotes = playerNotes,
        gameMasterNotes = gameMasterNotes,
        visible = visible
    )

private fun Faction.toEntity(cityId: String) =
    FactionEntity(
        id = id,
        cityId = cityId,
        name = name,
        description = description,
        playerNotes = playerNotes,
        gameMasterNotes = gameMasterNotes,
        visible = visible
    )
