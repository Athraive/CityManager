package de.geier.citymanager.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Repräsentiert eine eigenständige Stadt.
 *
 * Jede Stadt ist vollständig gekapselt.
 */
@Entity(tableName = "cities")
data class CityEntity(

    @PrimaryKey
    val id: String,

    val name: String,

    val gameMasterCode: String,

    val createdAt: Long,

    /* ---------------- Theme ---------------- */

    /**
     * UI-Theme der Stadt (modern, scifi, western, asia, medieval …)
     */
    val themePreset: String = "modern",

    /**
     * Hintergrundmodus:
     *  - "preset"  → aus App-Katalog
     *  - "custom"  → eigenes Bild (URI)
     */
    val backgroundMode: String = "preset",

    /**
     * Bei preset → Preset-Key
     * Bei custom → URI
     * null → kein Hintergrund
     */
    val backgroundValue: String? = null
)
