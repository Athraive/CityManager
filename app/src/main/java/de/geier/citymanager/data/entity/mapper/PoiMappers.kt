package de.geier.citymanager.data.mapper

import de.geier.citymanager.data.entity.PoiCategoryEntity
import de.geier.citymanager.data.entity.PointOfInterestEntity
import de.geier.citymanager.ui.PoiCategory
import de.geier.citymanager.ui.PointOfInterest
import de.geier.citymanager.ui.PoiType

fun PoiCategoryEntity.toModel(): PoiCategory =
    PoiCategory(
        id = id,
        title = title,
        icon = icon,
        backgroundImageUri = backgroundImageUri
    )

fun PoiCategory.toEntity(): PoiCategoryEntity =
    PoiCategoryEntity(
        id = id,
        title = title,
        icon = icon,
        backgroundImageUri = backgroundImageUri
    )

fun PointOfInterestEntity.toModel(): PointOfInterest =
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
        name = name,
        description = description,
        categoryId = categoryId,
        type = type.name,
        visible = visible
    )
