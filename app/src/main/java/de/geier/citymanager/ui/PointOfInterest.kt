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
     * Beschreibung des POI.
     * Inhalt ist optional, darf leer sein.
     */
    val description: String,

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
    val visible: Boolean,

    /**
     * Optionale Zuordnung zu einer Fraktion.
     *
     * - null  → keine Fraktion
     * - sonst → ID einer Faction
     */
    val factionId: String? = null
)
