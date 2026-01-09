package de.geier.citymanager.data.entity.mapper

import de.geier.citymanager.data.entity.PoiCategoryEntity
import de.geier.citymanager.ui.PoiCategory

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
