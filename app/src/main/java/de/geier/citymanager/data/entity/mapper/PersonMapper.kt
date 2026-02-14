package de.geier.citymanager.data.entity.mapper

import de.geier.citymanager.data.entity.PersonEntity
import de.geier.citymanager.ui.Person

fun PersonEntity.toDomain(): Person =
    Person(
        id = id,
        name = name,
        description = description,
        portraitImageUri = portraitImageUri,
        visible = visible,
        playerNotes = playerNotes,
        gameMasterNotes = gameMasterNotes,
        mapX = mapX,
        mapY = mapY
    )

/**
 * cityId wird bewusst vom Repository übergeben.
 */
fun Person.toEntity(cityId: String): PersonEntity =
    PersonEntity(
        id = id,
        cityId = cityId,
        name = name,
        description = description,
        portraitImageUri = portraitImageUri,
        visible = visible,
        playerNotes = playerNotes,
        gameMasterNotes = gameMasterNotes,
        mapX = mapX,
        mapY = mapY
    )
