package de.geier.citymanager.data

import androidx.room.Database
import androidx.room.RoomDatabase
import de.geier.citymanager.data.dao.*
import de.geier.citymanager.data.entity.*

@Database(
    entities = [
        /* --- Root Entity --- */
        CityEntity::class,

        /* --- Core Entities --- */
        CityDistrictEntity::class,
        CityLoreEntity::class,
        FactionEntity::class,
        PersonEntity::class,
        PointOfInterestEntity::class,
        PoiCategoryEntity::class,

        /* --- CrossRefs --- */
        PersonFactionCrossRef::class,
        PersonPoiCrossRef::class,
        PoiFactionCrossRef::class
    ],
    version = 13,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    /* --- Root DAO --- */
    abstract fun cityDao(): CityDao

    /* --- Core DAOs --- */
    abstract fun cityDistrictDao(): CityDistrictDao
    abstract fun cityLoreDao(): CityLoreDao
    abstract fun factionDao(): FactionDao
    abstract fun personDao(): PersonDao
    abstract fun pointOfInterestDao(): PointOfInterestDao
    abstract fun poiCategoryDao(): PoiCategoryDao

    /* --- CrossRef DAOs --- */
    abstract fun personFactionDao(): PersonFactionDao
    abstract fun personPoiDao(): PersonPoiDao
    abstract fun poiFactionDao(): PoiFactionDao
}
