package de.geier.citymanager.data.preferences

import android.content.Context
import androidx.datastore.preferences.core.floatPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

// 🔧 DataStore Extension
private val Context.dataStore by preferencesDataStore(name = "user_prefs")

class UserPreferences(private val context: Context) {

    companion object {
        private val FONT_SCALE = floatPreferencesKey("font_scale")
    }

    // 👉 lesen
    val fontScale: Flow<Float> =
        context.dataStore.data.map { preferences ->
            preferences[FONT_SCALE] ?: 1.0f
        }

    // 👉 speichern
    suspend fun setFontScale(value: Float) {
        context.dataStore.edit { preferences ->
            preferences[FONT_SCALE] = value
        }
    }
}