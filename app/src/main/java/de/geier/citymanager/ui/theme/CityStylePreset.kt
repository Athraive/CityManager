package de.geier.citymanager.ui.theme

enum class CityStylePreset {
    SCIFI,
    FANTASY,
    ASIA,
    WESTERN;

    companion object {
        fun from(value: String?): CityStylePreset =
            entries.firstOrNull { it.name == value } ?: SCIFI
    }
}