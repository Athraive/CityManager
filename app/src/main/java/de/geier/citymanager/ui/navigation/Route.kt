package com.yourpackage.citymanager.navigation

sealed class Route(val route: String) {

    /**
     * Top-Level Navigation
     */
    object Karte : Route("karte")
    object Personen : Route("personen")
    object Pois : Route("pois")

    /**
     * Unter-Navigation innerhalb des Karten-Tabs
     */
    object Stadtviertel : Route("karte/stadtviertel")
    object Stadtgeschichte : Route("karte/geschichte")

    /**
     * Detail-Screen für ein Stadtviertel
     * (wird später gebraucht, jetzt schon stabil vorbereitet)
     */
    object StadtviertelDetail : Route("karte/stadtviertel/{districtId}") {
        fun create(districtId: Long): String =
            "karte/stadtviertel/$districtId"
    }
}
