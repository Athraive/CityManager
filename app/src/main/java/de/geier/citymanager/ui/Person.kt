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
     * Öffentliche Beschreibung der Person.
     *
     * - vom Spielleiter gepflegt
     * - für Spieler sichtbar
     * - editierbar (kein reines Anlagefeld!)
     */
    val description: String = "",

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
     * Spieler-Notizen zur Person.
     *
     * - von Spielern gepflegt
     * - für Spieler sichtbar
     * - für SL ebenfalls sichtbar
     */
    val playerNotes: String = "",

    /**
     * Interne Spielleiter-Notizen zur Person.
     *
     * - nur für den Spielleiter sichtbar
     * - enthält Meta-, Plot- oder Geheim-Informationen
     */
    val gameMasterNotes: String = ""
)
