package de.geier.citymanager.ui

/**
 * Repräsentiert eine Fraktion innerhalb der Stadt.
 *
 * Fraktionen sind optionale Gruppierungen, denen später
 * POIs und Personen zugeordnet werden können.
 *
 * WICHTIG:
 * - Dieses Datenmodell enthält NOCH keine Logik
 * - Keine Verknüpfungen zu POI oder Personen
 * - Keine UI-spezifischen Elemente
 */
data class Faction(
    /**
     * Eindeutige ID der Fraktion.
     * Wird später z. B. für Zuordnungen verwendet.
     */
    val id: String,

    /**
     * Anzeigename der Fraktion.
     * Pflichtfeld – jede Fraktion muss einen Namen haben.
     */
    val name: String,

    /**
     * Optionale Beschreibung.
     * Kann z. B. Ziele, Ideologie oder Hintergrund enthalten.
     */
    val description: String? = null,

    /**
     * Gibt an, ob die Fraktion für Spieler sichtbar ist.
     * Spielleiter sehen Fraktionen immer.
     */
    val visible: Boolean = true
)
