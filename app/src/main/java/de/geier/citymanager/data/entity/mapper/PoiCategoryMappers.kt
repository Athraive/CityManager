package de.geier.citymanager.data.entity.mapper

import de.geier.citymanager.data.entity.PoiCategoryEntity
import de.geier.citymanager.ui.PoiCategory

fun PoiCategoryEntity.toDomain(): PoiCategory =
    PoiCategory(
        id = id,
        title = title,
        icon = icon,
        description = description,
        backgroundImageUri = backgroundImageUri,
        visible = visible
    )

fun PoiCategory.toEntity(cityId: String): PoiCategoryEntity =
    PoiCategoryEntity(
        id = id,
        cityId = cityId,
        title = title,
        icon = icon,
        description = description,
        backgroundImageUri = backgroundImageUri,
        visible = visible
    )
