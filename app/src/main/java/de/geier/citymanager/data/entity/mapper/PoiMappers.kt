package de.geier.citymanager.data.entity.mapper

import de.geier.citymanager.data.entity.PoiCategoryEntity
import de.geier.citymanager.data.entity.PointOfInterestEntity
import de.geier.citymanager.ui.PoiCategory
import de.geier.citymanager.ui.PointOfInterest
import de.geier.citymanager.ui.PoiType

/* ---------- Kategorie ---------- */

fun PoiCategoryEntity.toDomain(): PoiCategory =
    PoiCategory(
        id = id,
        title = title,
        icon = icon,
        backgroundImageUri = backgroundImageUri,
        visible = visible
    )

fun PoiCategory.toEntity(): PoiCategoryEntity =
    PoiCategoryEntity(
        id = id,
        title = title,
        icon = icon,
        backgroundImageUri = backgroundImageUri,
        visible = visible
    )

/* ---------- Point of Interest ---------- */

fun PointOfInterestEntity.toDomain(): PointOfInterest =
    PointOfInterest(
        id = id,
        name = name,
        description = description,
        categoryId = categoryId,
        type = PoiType.valueOf(type),
        visible = visible
    )

fun PointOfInterest.toEntity(): PointOfInterestEntity =
    PointOfInterestEntity(
        id = id,
        categoryId = categoryId,
        name = name,
        description = description,
        type = type.name,
        visible = visible
    )
