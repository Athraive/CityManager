package de.geier.citymanager.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "city_districts")
data class CityDistrictEntity(
    @PrimaryKey
    val id: String,
    val cityId: String,
    val name: String,
    val description: String,
    val orderIndex: Int,
    val mapKey: String? = null
)
