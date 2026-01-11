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
        factionId = factionId,
        sharedNotes = sharedNotes
    )

fun Person.toEntity(): PersonEntity =
    PersonEntity(
        id = id,
        name = name,
        description = description,
        portraitImageUri = portraitImageUri,
        visible = visible,
        factionId = factionId,
        sharedNotes = sharedNotes
    )
