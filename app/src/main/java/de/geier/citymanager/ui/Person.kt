package de.geier.citymanager.ui

/**
 * Domain-Modell für Personen (NPCs).
 *
 * Fraktionen werden NICHT mehr direkt gehalten,
 * sondern ausschließlich über PersonFactionCrossRef.
 */
data class Person(
    val id: String,
    val name: String,
    val description: String,
    val portraitImageUri: String? = null,
    val visible: Boolean = true,
    val playerNotes: String = "",
    val gameMasterNotes: String = "",

    // Kartenkoordinaten (optional, normalisiert 0f–1f)
    val mapX: Float? = null,
    val mapY: Float? = null
)
