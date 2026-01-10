package de.geier.citymanager.ui

/**
 * Repräsentiert eine Person (NPC) innerhalb einer Stadt.
 *
 * Dieses Modell wird in der UI und im ViewModel verwendet.
 * Es enthält keine Persistenz- oder UI-Logik.
 */
data class Person(
    /**
     * Eindeutige ID der Person.
     */
    val id: String,

    /**
     * Anzeigename der Person.
     */
    val name: String,

    /**
     * Optionale Beschreibung der Person.
     */
    val description: String? = null,

    /**
     * Optionale URI zu einem Portraitbild.
     */
    val portraitImageUri: String? = null,

    /**
     * Gibt an, ob die Person für Spieler sichtbar ist.
     */
    val visible: Boolean = true,

    /**
     * Optionale Zuordnung zu einer Fraktion.
     */
    val factionId: String? = null,

    /**
     * Gemeinsamer Notizbereich für SL und Spieler.
     */
    val sharedNotes: String = ""
)
