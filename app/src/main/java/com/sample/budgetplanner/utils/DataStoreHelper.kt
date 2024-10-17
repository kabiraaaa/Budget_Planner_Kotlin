package com.sample.budgetplanner.utils

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.doublePreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

object DataStoreHelper {

    val Context.dataStore by preferencesDataStore(name = "user_preferences")
    private val ON_BOARDING_KEY = booleanPreferencesKey("on_boarded")
    private val NAME = stringPreferencesKey("name")
    private val AGE = stringPreferencesKey("user_age")
    private val GENDER = stringPreferencesKey("gender")
    private val GOAL = stringSetPreferencesKey("user_goals")
    private val MONTHLY_SPEND = doublePreferencesKey("monthly_spend")
    private val MONTHLY_SALARY = doublePreferencesKey("monthly_salary")

    suspend fun saveOnBoardingStatus(context: Context, isOnBoardingDone: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[ON_BOARDING_KEY] = isOnBoardingDone
        }
    }

    suspend fun saveUserName(context: Context, name: String) {
        context.dataStore.edit { preferences ->
            preferences[NAME] = name
        }
    }

    suspend fun saveUserAge(context: Context, age: String) {
        context.dataStore.edit { preferences ->
            preferences[AGE] = age
        }
    }

    suspend fun saveUserGender(context: Context, gender: String) {
        context.dataStore.edit { preferences ->
            preferences[GENDER] = gender
        }
    }

    suspend fun saveUserGoals(context: Context, goals: Set<String>) {
        context.dataStore.edit { preferences ->
            preferences[GOAL] = goals
        }
    }


    suspend fun saveMonthlySpend(context: Context, spend: Double) {
        context.dataStore.edit { preferences ->
            preferences[MONTHLY_SPEND] = spend
        }
    }

    suspend fun saveMonthlySalary(context: Context, salary: Double) {
        context.dataStore.edit { preferences ->
            preferences[MONTHLY_SALARY] = salary
        }
    }


    fun getOnBoardingStatus(context: Context): Flow<Boolean> {
        return context.dataStore.data.map { preferences ->
            preferences[ON_BOARDING_KEY] ?: false
        }
    }

    fun getUserName(context: Context): Flow<String> {
        return context.dataStore.data.map { preferences ->
            preferences[NAME] ?: ""
        }
    }

    fun getUserAge(context: Context): Flow<String> {
        return context.dataStore.data.map { preferences ->
            preferences[AGE] ?: ""
        }
    }

    fun getUserGender(context: Context): Flow<String> {
        return context.dataStore.data.map { preferences ->
            preferences[GENDER] ?: ""
        }
    }

    fun getUserGoals(context: Context): Flow<Set<String>> {
        return context.dataStore.data.map { preferences ->
            preferences[GOAL] ?: emptySet()
        }
    }

    fun getMonthlySpend(context: Context): Flow<Double> {
        return context.dataStore.data.map { preferences ->
            preferences[MONTHLY_SPEND] ?: 0.0
        }
    }

    fun getMonthlySalary(context: Context): Flow<Double> {
        return context.dataStore.data.map { preferences ->
            preferences[MONTHLY_SALARY] ?: 0.0
        }
    }

}