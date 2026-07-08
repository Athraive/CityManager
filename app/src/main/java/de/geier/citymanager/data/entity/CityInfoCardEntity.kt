package de.geier.citymanager.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "city_info_cards")
data class CityInfoCardEntity(

    @PrimaryKey
    val id: String,

    val cityId: String,

    val title: String,

    val content: String,

    /** URI des optionalen Bildes dieser Card */
    val imageUri: String? = null,

    val visible: Boolean = true,

    val order: Int = 0
)