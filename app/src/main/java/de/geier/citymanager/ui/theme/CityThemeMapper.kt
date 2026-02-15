package de.geier.citymanager.ui.theme

import de.geier.citymanager.data.entity.CityEntity

fun CityEntity.toCityTheme(): CityTheme =
    CityTheme(
        backgroundPreset = BackgroundPreset.from(backgroundPreset),
        backgroundImageUri = backgroundImageUri,
        fontPreset = FontPreset.from(fontPreset)
    )
