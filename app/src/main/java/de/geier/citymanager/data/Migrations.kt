package de.geier.citymanager.data

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

val MIGRATION_6_7 = object : Migration(6, 7) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL("ALTER TABLE pois ADD COLUMN imageUri TEXT")
        db.execSQL("ALTER TABLE factions ADD COLUMN imageUri TEXT")
    }
}