package de.geier.citymanager.ui

/**
 * Repräsentiert einen Punkt von Interesse (POI) innerhalb der Stadt.
 *
 * POIs können optional einer Fraktion zugeordnet sein.
 * Wenn factionId == null, gehört der POI zu keiner Fraktion.
 */
data class PointOfInterest(

    /**
     * Eindeutige ID des POI.
     */
    val id: String,

    /**
     * Anzeigename des POI.
     */
    val name: String,

    /**
     * Öffentliche Beschreibung des POI.
     *
     * - vom Spielleiter gepflegt
     * - für Spieler sichtbar
     * - editierbar
     */
    val description: String = "",

    /**
     * Kategorie-ID, zu der dieser POI gehört.
     */
    val categoryId: String,

    /**
     * Typ des POI (z. B. Ort oder Geschäft).
     */
    val type: PoiType,

    /**
     * Gibt an, ob der POI für Spieler sichtbar ist.
     */
    val visible: Boolean = true,

    /**
     * Optionale Zuordnung zu einer Fraktion.
     *
     * - null  → keine Fraktion
     * - sonst → ID einer Faction
     */
    val factionId: String? = null,

    /**
     * Spieler-Notizen zum POI.
     *
     * - von Spielern gepflegt
     * - für Spieler sichtbar
     * - für SL ebenfalls sichtbar
     */
    val playerNotes: String = "",

    /**
     * Interne Spielleiter-Notizen zum POI.
     *
     * - nur für den Spielleiter sichtbar
     * - enthält Meta-, Plot- oder Geheim-Informationen
     */
    val gameMasterNotes: String = ""
)
