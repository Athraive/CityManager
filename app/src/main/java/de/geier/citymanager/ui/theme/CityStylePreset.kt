package de.geier.citymanager.ui.theme

enum class CityStylePreset {
    URBAN_GREY,
    PARCHEMENT,
    BLOSSOM,
    DUST,
    FILM_NOIR,
    NEON_MATRIX;

    companion object {
        fun from(value: String?): CityStylePreset =
            entries.firstOrNull { it.name == value } ?: URBAN_GREY
    }
}