package de.geier.citymanager.ui.data

import de.geier.citymanager.ui.PoiCategory

/**
 * TEMPORÄRE Dummy-Daten
 * Wird später durch Repository / DB ersetzt
 */
val POI_CATEGORIES = listOf(
    PoiCategory(
        id = "tavern",
        title = "Tavernen",
        icon = "🍺",
        visible = true
    ),
    PoiCategory(
        id = "market",
        title = "Märkte",
        icon = "🛒",
        visible = true
    ),
    PoiCategory(
        id = "temple",
        title = "Tempel",
        icon = "⛪",
        visible = false
    )
)
