package de.geier.citymanager.ui

/**
 * Kategorie für Points of Interest (z. B. Tavernen, Verwaltung, Läden …)
 *
 * @param backgroundImageUri Optionales Hintergrundbild, das der Spielleiter
 *        später als Content pflegen kann (z. B. saisonal).
 */
data class PoiCategory(
    val id: String,
    val title: String,
    val icon: String,
    val backgroundImageUri: String? = null
)

/**
 * Vordefinierte Kategorien (können später editierbar gemacht werden)
 */
val POI_CATEGORIES = listOf(
    PoiCategory(
        id = "tavern",
        title = "Gasthäuser & Tavernen",
        icon = "🍺"
    ),
    PoiCategory(
        id = "public",
        title = "Verwaltung & Religion",
        icon = "🏛️"
    ),
    PoiCategory(
        id = "craft",
        title = "Handwerk & Dienstleistungen",
        icon = "🛠️"
    ),
    PoiCategory(
        id = "transport",
        title = "Transport & Verkehr",
        icon = "🚚"
    ),
    PoiCategory(
        id = "shops",
        title = "Läden & Geschäfte",
        icon = "🛒"
    ),
    PoiCategory(
        id = "fun",
        title = "Spaß & Vergnügen",
        icon = "🎭"
    )
)
