package de.geier.citymanager.data.entity.mapper

import de.geier.citymanager.data.entity.PointOfInterestEntity
import de.geier.citymanager.ui.PointOfInterest
import de.geier.citymanager.ui.PoiType

fun PointOfInterestEntity.toDomain(): PointOfInterest =
    PointOfInterest(
        id = id,
        categoryId = categoryId,
        name = name,
        description = description,
        type = PoiType.valueOf(type),
        visible = visible,
        factionId = factionId          // 🔹 NEU
    )

fun PointOfInterest.toEntity(): PointOfInterestEntity =
    PointOfInterestEntity(
        id = id,
        categoryId = categoryId,
        name = name,
        description = description,
        type = type.name,
        visible = visible,
        factionId = factionId          // 🔹 NEU
    )
