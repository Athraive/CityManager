package de.geier.citymanager.data.entity

import androidx.room.Entity

/**
 * Cross-Reference zwischen POI und Fraktion.
 *
 * - Ein POI kann mehreren Fraktionen angehören
 * - Eine Fraktion kann mehreren POIs zugeordnet sein
 */
@Entity(
    tableName = "poi_faction_cross_ref",
    primaryKeys = ["poiId", "factionId"]
)
data class PoiFactionCrossRef(
    val poiId: String,
    val factionId: String
)
