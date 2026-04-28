package de.geier.citymanager.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Repräsentiert eine eigenständige Stadt.
 */
@Entity(tableName = "cities")
data class CityEntity(

    @PrimaryKey
    val id: String,

    val name: String,

    val gameMasterCode: String,

    val createdAt: Long,

    /* =====================================================
 * THEME
 * ===================================================== */

    val backgroundPreset: String = "WHITE",
    val backgroundImageUri: String? = null,
    val fontPreset: String = "DEFAULT",
    val stylePreset: String = "SCIFI",

    /* =====================================================
     * OVERVIEW (NEU)
     * ===================================================== */

    val coatOfArmsUri: String? = null,

    val country: String = "",
    val region: String = "",
    val language: String = "",
    val government: String = "",
    val elevation: String = "",
    val area: String = "",
    val population: String = "",
    val description: String = ""
)