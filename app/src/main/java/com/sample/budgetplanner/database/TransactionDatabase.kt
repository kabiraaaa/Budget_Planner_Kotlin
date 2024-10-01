package com.sample.budgetplanner.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.sample.budgetplanner.dao.TransactionsDao
import com.sample.budgetplanner.models.Transactions

@Database(entities = [Transactions::class], version = 1)
abstract class TransactionDatabase : RoomDatabase() {
    abstract fun transactionDao(): TransactionsDao
}