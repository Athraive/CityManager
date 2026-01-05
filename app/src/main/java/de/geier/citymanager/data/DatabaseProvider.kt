package de.geier.citymanager.data

import android.content.Context
import androidx.room.Room
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

object DatabaseProvider {

    @Volatile
    private var INSTANCE: AppDatabase? = null

    private val MIGRATION_1_2 = object : Migration(1, 2) {
        override fun migrate(database: SupportSQLiteDatabase) {
            database.execSQL(
                """
                ALTER TABLE pois
                ADD COLUMN visible INTEGER NOT NULL DEFAULT 1
                """.trimIndent()
            )
        }
    }

    fun getDatabase(context: Context): AppDatabase {
        return INSTANCE ?: synchronized(this) {
            val instance = Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java,
                "city_manager.db"
            )
                .addMigrations(MIGRATION_1_2)
                .build()
            INSTANCE = instance
            instance
        }
    }
}
