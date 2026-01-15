package de.geier.citymanager.data

import androidx.room.Database
import androidx.room.RoomDatabase
import de.geier.citymanager.data.dao.*
import de.geier.citymanager.data.entity.*

@Database(
    entities = [
        // POI / Kategorien
        PoiCategoryEntity::class,
        PointOfInterestEntity::class,

        // Personen
        PersonEntity::class,
        PersonPoiCrossRef::class,

        // Stadt
        CityDistrictEntity::class,
        CityLoreEntity::class            // ✅ Stadtgeschichte
    ],
    version = 7,                        // 🔺 VERSION ERHÖHT (neu: CityLore)
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    // POI / Kategorien
    abstract fun poiCategoryDao(): PoiCategoryDao
    abstract fun pointOfInterestDao(): PointOfInterestDao

    // Personen
    abstract fun personDao(): PersonDao
    abstract fun personPoiDao(): PersonPoiDao

    // Stadt
    abstract fun cityDistrictDao(): CityDistrictDao
    abstract fun cityLoreDao(): CityLoreDao
}
