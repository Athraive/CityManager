package de.geier.citymanager.ui

data class PointOfInterest(
    val id: String,
    val name: String,

    val shortDescription: String = "",
    val description: String?,
    val imageUri: String? = null,
    val categoryId: String,
    val visible: Boolean,
    val factionId: String?,
    val playerNotes: String,
    val gameMasterNotes: String,

    // NEU
    val mapX: Float? = null,
    val mapY: Float? = null
)
