package com.sample.budgetplanner

import android.app.Application
import androidx.room.Room
import com.sample.budgetplanner.database.TransactionDatabase

class MyApp : Application() {
    companion object {
        lateinit var database: TransactionDatabase
            private set
    }

    override fun onCreate() {
        super.onCreate()
        database = Room.databaseBuilder(
            applicationContext,
            TransactionDatabase::class.java,
            "transactions_db"
        ).build()
    }
}