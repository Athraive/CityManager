package de.geier.citymanager.ui.theme

enum class BackgroundPreset {
    WHITE,
    MEDIEVAL,
    SCIFI,
    WESTERN,
    ASIA;

    companion object {
        fun from(value: String?): BackgroundPreset =
            entries.firstOrNull { it.name == value } ?: WHITE
    }
}
