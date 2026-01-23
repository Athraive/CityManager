package de.geier.citymanager.data.entity

import androidx.room.Entity

/**
 * Cross-Reference zwischen Person und Fraktion.
 *
 * - Eine Person kann mehreren Fraktionen angehören
 * - Eine Fraktion kann mehreren Personen zugeordnet sein
 */
@Entity(
    tableName = "person_faction_cross_ref",
    primaryKeys = ["personId", "factionId"]
)
data class PersonFactionCrossRef(
    val personId: String,
    val factionId: String
)
