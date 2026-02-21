package de.geier.citymanager.ui.navigation

object Route {

    // Über die Stadt
    const val STADTUEBERSICHT = "stadtuebersicht"
    const val STADTKARTE = "stadtkarte"
    const val STADTVIERTEL_LIST = "stadtviertel"
    const val STADTVIERTEL_DETAIL = "stadtviertel/{districtId}"
    const val STADTGESCHICHTE = "stadtgeschichte"

    // Weitere Tabs
    const val PERSONEN = "personen"
    const val POIS = "pois"
    const val FRAKTIONEN = "fraktionen"

    // Pin Management
    const val MANAGE_PINS = "manage_pins"
    const val ADD_PIN = "add_pin/{x}/{y}"
}