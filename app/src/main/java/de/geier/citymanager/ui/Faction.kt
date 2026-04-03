package de.geier.citymanager.ui

/**
 * Repräsentiert eine Fraktion innerhalb der Stadt.
 */
data class Faction(
    val id: String,
    val name: String,
    val description: String? = null,

    val imageUri: String? = null,   // ✅ HIER IST DER FIX

    /** Spieler-Notizen (gemeinsamer Pool) */
    val playerNotes: String = "",

    /** SL-exklusive Notizen */
    val gameMasterNotes: String = "",

    /** Sichtbarkeit für Spieler */
    val visible: Boolean = true
)