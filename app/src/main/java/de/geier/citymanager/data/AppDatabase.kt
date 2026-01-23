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

        // Fraktionen (neu)
        FactionEntity::class,
        PersonFactionCrossRef::class,
        PoiFactionCrossRef::class,

        // Stadt
        CityDistrictEntity::class,
        CityLoreEntity::class
    ],
    version = 8,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    // POI / Kategorien
    abstract fun poiCategoryDao(): PoiCategoryDao
    abstract fun pointOfInterestDao(): PointOfInterestDao

    // Personen
    abstract fun personDao(): PersonDao
    abstract fun personPoiDao(): PersonPoiDao

    // Fraktionen (neu)
    abstract fun factionDao(): FactionDao
    abstract fun personFactionDao(): PersonFactionDao
    abstract fun poiFactionDao(): PoiFactionDao

    // Stadt
    abstract fun cityDistrictDao(): CityDistrictDao
    abstract fun cityLoreDao(): CityLoreDao
}
