package de.geier.citymanager.data

import androidx.room.Database
import androidx.room.RoomDatabase
import de.geier.citymanager.data.dao.PoiCategoryDao
import de.geier.citymanager.data.dao.PointOfInterestDao
import de.geier.citymanager.data.entity.PoiCategoryEntity
import de.geier.citymanager.data.entity.PointOfInterestEntity

@Database(
    entities = [
        PoiCategoryEntity::class,
        PointOfInterestEntity::class
    ],
    version = 2,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun poiCategoryDao(): PoiCategoryDao
    abstract fun pointOfInterestDao(): PointOfInterestDao
}
