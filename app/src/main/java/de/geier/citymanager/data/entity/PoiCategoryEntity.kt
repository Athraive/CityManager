package de.geier.citymanager.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "poi_categories")
data class PoiCategoryEntity(
    @PrimaryKey
    val id: String,

    val title: String,
    val icon: String,
    val backgroundImageUri: String?
)
