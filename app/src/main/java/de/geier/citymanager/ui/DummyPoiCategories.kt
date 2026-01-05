package de.geier.citymanager.ui

data class PoiCategoryUi(
    val id: String,
    val title: String,
    val icon: String
)

val DUMMY_POI_CATEGORIES = listOf(
    PoiCategoryUi(
        id = "tavern",
        title = "Gasthäuser & Tavernen",
        icon = "🍺"
    ),
    PoiCategoryUi(
        id = "public",
        title = "Verwaltung & Religion",
        icon = "🏛️"
    ),
    PoiCategoryUi(
        id = "craft",
        title = "Handwerk & Dienstleistungen",
        icon = "🛠️"
    ),
    PoiCategoryUi(
        id = "shops",
        title = "Läden & Geschäfte",
        icon = "🛒"
    ),
    PoiCategoryUi(
        id = "fun",
        title = "Spaß & Vergnügen",
        icon = "🎭"
    )
)
