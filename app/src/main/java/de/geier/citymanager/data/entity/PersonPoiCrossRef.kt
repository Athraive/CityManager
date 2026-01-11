package de.geier.citymanager.data.entity

import androidx.room.Entity

/**
 * Cross-Reference zwischen Person und POI.
 *
 * Bedeutet:
 * - Eine Person kann mehreren POIs zugeordnet sein
 * - Ein POI kann mehreren Personen zugeordnet sein
 *
 * Enthält bewusst KEINE weitere Logik.
 */
@Entity(
    tableName = "person_poi_cross_ref",
    primaryKeys = ["personId", "poiId"]
)
data class PersonPoiCrossRef(
    val personId: String,
    val poiId: String
)
