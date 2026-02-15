package de.geier.citymanager.ui.theme

enum class FontPreset {
    DEFAULT,
    MEDIEVAL,
    SCIFI,
    WESTERN,
    ASIA;

    companion object {
        fun from(value: String?): FontPreset =
            entries.firstOrNull { it.name == value } ?: DEFAULT
    }
}
