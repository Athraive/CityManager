package de.geier.citymanager.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "persons")
data class PersonEntity(
    @PrimaryKey
    val id: String,
    val name: String,
    val description: String?,
    val portraitImageUri: String?,
    val visible: Boolean,
    val factionId: String?,
    val sharedNotes: String
)
