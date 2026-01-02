package de.geier.citymanager.ui

/**
 * Repräsentiert einen Ort oder ein Geschäft innerhalb der Stadt.
 *
 * @param id eindeutige ID (z. B. UUID)
 * @param name Anzeigename des Ortes
 * @param description Freitext-Beschreibung
 * @param categoryId Kategorie-Zuordnung (z. B. "tavern", "public", ...)
 * @param type Typ des POI (LOCATION oder SHOP)
 * @param visible Ob der POI für Spieler sichtbar ist
 */
data class PointOfInterest(
    val id: String,
    val name: String,
    val description: String,
    val categoryId: String,
    val type: PoiType,
    val visible: Boolean
)
