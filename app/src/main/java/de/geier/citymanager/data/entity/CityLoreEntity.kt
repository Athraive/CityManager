package de.geier.citymanager.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "city_lore")
data class CityLoreEntity(
    @PrimaryKey
    val cityId: String,

    val title: String,
    val text: String,

    // NEU: Kartenbild der Stadt
    val mapImageUri: String? = null
)
