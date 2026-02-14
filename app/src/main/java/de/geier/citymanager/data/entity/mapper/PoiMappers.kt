package de.geier.citymanager.data.entity.mapper

import de.geier.citymanager.data.entity.PointOfInterestEntity
import de.geier.citymanager.ui.PointOfInterest

fun PointOfInterestEntity.toUi(): PointOfInterest =
    PointOfInterest(
        id = id,
        name = name,
        description = description,
        categoryId = categoryId,
        visible = visible,
        factionId = factionId,
        playerNotes = playerNotes,
        gameMasterNotes = gameMasterNotes,
        mapX = mapX,
        mapY = mapY
    )

/**
 * cityId wird bewusst vom Repository übergeben.
 */
fun PointOfInterest.toEntity(cityId: String): PointOfInterestEntity =
    PointOfInterestEntity(
        id = id,
        cityId = cityId,
        name = name,
        description = description,
        categoryId = categoryId,
        visible = visible,
        factionId = factionId,
        playerNotes = playerNotes,
        gameMasterNotes = gameMasterNotes,
        mapX = mapX,
        mapY = mapY
    )
