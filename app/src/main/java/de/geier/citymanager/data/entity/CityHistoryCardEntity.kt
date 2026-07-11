package de.geier.citymanager.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "city_history_cards")
data class CityHistoryCardEntity(

    @PrimaryKey
    val id: String,

    val cityId: String,

    val title: String,

    val content: String,

    /** Optionales Bild dieser Geschichtskarte */
    val imageUri: String? = null,

    val visible: Boolean = true,

    val order: Int = 0
)