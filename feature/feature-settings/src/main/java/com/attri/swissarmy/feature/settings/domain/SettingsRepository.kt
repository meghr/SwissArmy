package com.attri.swissarmy.feature.settings.domain

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

@Singleton
class SettingsRepository @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val THEME_KEY = stringPreferencesKey("theme_mode")
    private val SCAN_QUALITY_KEY = intPreferencesKey("scan_quality")
    private val COMPRESSION_LEVEL_KEY = intPreferencesKey("compression_level")

    val themeMode: Flow<String> = context.dataStore.data.map { preferences ->
        preferences[THEME_KEY] ?: "System"
    }

    val scanQuality: Flow<Int> = context.dataStore.data.map { preferences ->
        preferences[SCAN_QUALITY_KEY] ?: 80
    }
    
    val compressionLevel: Flow<Int> = context.dataStore.data.map { preferences ->
        preferences[COMPRESSION_LEVEL_KEY] ?: 50
    }

    suspend fun setThemeMode(mode: String) {
        context.dataStore.edit { preferences ->
            preferences[THEME_KEY] = mode
        }
    }

    suspend fun setScanQuality(quality: Int) {
        context.dataStore.edit { preferences ->
            preferences[SCAN_QUALITY_KEY] = quality
        }
    }
    
    suspend fun setCompressionLevel(level: Int) {
        context.dataStore.edit { preferences ->
            preferences[COMPRESSION_LEVEL_KEY] = level
        }
    }
    
    suspend fun clearCache(): Long {
        return try {
            val cacheDir = context.cacheDir
            val size = cacheDir.walkTopDown().map { it.length() }.sum()
            cacheDir.deleteRecursively()
            size
        } catch (e: Exception) {
            -1L
        }
    }
}
