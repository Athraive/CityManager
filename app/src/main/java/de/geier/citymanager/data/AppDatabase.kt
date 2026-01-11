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
        PersonPoiCrossRef::class // 🔹 NEU
    ],
    version = 5,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun poiCategoryDao(): PoiCategoryDao
    abstract fun pointOfInterestDao(): PointOfInterestDao
    abstract fun personDao(): PersonDao

    // 🔹 NEU
    abstract fun personPoiDao(): PersonPoiDao
}
