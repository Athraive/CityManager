package de.geier.citymanager.data.entity.mapper

import de.geier.citymanager.data.entity.PointOfInterestEntity
import de.geier.citymanager.ui.PointOfInterest
import de.geier.citymanager.ui.PoiType

/**
 * Mapper zwischen Persistenz-Entity und Domain-Modell für POIs.
 */

/* ---------------- Entity → Domain ---------------- */

fun PointOfInterestEntity.toDomain(): PointOfInterest =
    PointOfInterest(
        id = id,
        categoryId = categoryId,
        name = name,
        description = description,
        type = PoiType.valueOf(type),
        visible = visible,
        factionId = factionId,
        playerNotes = playerNotes,
        gameMasterNotes = gameMasterNotes
    )

/* ---------------- Domain → Entity ---------------- */

fun PointOfInterest.toEntity(): PointOfInterestEntity =
    PointOfInterestEntity(
        id = id,
        categoryId = categoryId,
        name = name,
        description = description,
        type = type.name,
        visible = visible,
        factionId = factionId,
        playerNotes = playerNotes,
        gameMasterNotes = gameMasterNotes
    )
