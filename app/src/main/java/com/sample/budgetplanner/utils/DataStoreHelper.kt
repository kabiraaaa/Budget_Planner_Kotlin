package com.sample.budgetplanner.utils

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

object DataStoreHelper {

    val Context.dataStore by preferencesDataStore(name = "user_preferences")
    private val ON_BOARDING_KEY = booleanPreferencesKey("on_boarded")

    suspend fun saveOnBoardingStatus(context: Context, isOnBoardingDone: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[ON_BOARDING_KEY] = isOnBoardingDone
        }
    }

    fun getOnBoardingStatus(context: Context): Flow<Boolean> {
        return context.dataStore.data.map { preferences ->
            preferences[ON_BOARDING_KEY] ?: false
        }
    }
}