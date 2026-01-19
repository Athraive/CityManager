package de.geier.citymanager.data.entity.mapper

import de.geier.citymanager.data.entity.PersonEntity
import de.geier.citymanager.ui.Person

/**
 * Mapper zwischen Persistenz-Entity und Domain-Modell für Personen.
 *
 * Enthält KEINE Logik, nur Feld-zu-Feld-Zuordnung.
 */
fun PersonEntity.toDomain(): Person =
    Person(
        id = id,
        name = name,
        description = description,
        portraitImageUri = portraitImageUri,
        visible = visible,
        factionId = factionId,
        playerNotes = playerNotes,
        gameMasterNotes = gameMasterNotes
    )

fun Person.toEntity(): PersonEntity =
    PersonEntity(
        id = id,
        name = name,
        description = description,
        portraitImageUri = portraitImageUri,
        visible = visible,
        factionId = factionId,
        playerNotes = playerNotes,
        gameMasterNotes = gameMasterNotes
    )
