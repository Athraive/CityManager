package de.geier.citymanager.data

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

val MIGRATION_6_7 = object : Migration(6, 7) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL("ALTER TABLE pois ADD COLUMN imageUri TEXT")
        db.execSQL("ALTER TABLE factions ADD COLUMN imageUri TEXT")
    }
}

val MIGRATION_20_21 = object : Migration(20, 21) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL(
            "ALTER TABLE city_districts " +
                    "ADD COLUMN subtitle TEXT NOT NULL DEFAULT ''"
        )
    }
}