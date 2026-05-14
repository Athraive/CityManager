package de.geier.citymanager.ui.theme

fun FontPreset.displayName(): String =
    when (this) {

        FontPreset.DEFAULT ->
            "Default"

        FontPreset.SCIFI ->
            "Future World"

        FontPreset.WESTERN ->
            "Western Frontier"

        FontPreset.ASIA ->
            "Jade Empire"

        FontPreset.MEDIEVAL ->
            "Old Kingdom"
    }

fun CityStylePreset.displayName(): String =
    when (this) {

        CityStylePreset.URBAN_GREY ->
            "Urban Grey"

        CityStylePreset.PARCHEMENT ->
            "Worn Parchement"

        CityStylePreset.BLOSSOM ->
            "Cherry Blossom"

        CityStylePreset.DUST ->
            "Dusty Road"

        CityStylePreset.FILM_NOIR ->
            "Film Noir"

        CityStylePreset.NEON_MATRIX ->
            "Neon Matrix"
    }