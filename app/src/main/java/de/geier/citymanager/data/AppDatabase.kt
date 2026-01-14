package de.geier.citymanager.data

import androidx.room.Database
import androidx.room.RoomDatabase
import de.geier.citymanager.data.dao.*
import de.geier.citymanager.data.entity.*

@Database(
    entities = [
        PoiCategoryEntity::class,
        PointOfInterestEntity::class,
        PersonEntity::class,
        PersonPoiCrossRef::class,
        CityDistrictEntity::class      // ✅ HINZUFÜGEN
    ],
    version = 6,                      // 🔺 VERSION ERHÖHEN
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun poiCategoryDao(): PoiCategoryDao
    abstract fun pointOfInterestDao(): PointOfInterestDao
    abstract fun personDao(): PersonDao
    abstract fun personPoiDao(): PersonPoiDao

    abstract fun cityDistrictDao(): CityDistrictDao   // ✅ HINZUFÜGEN
}
