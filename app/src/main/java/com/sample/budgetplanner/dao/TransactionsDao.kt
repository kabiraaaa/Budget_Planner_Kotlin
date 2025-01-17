package com.sample.budgetplanner.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.sample.budgetplanner.models.Transactions

@Dao
interface TransactionsDao {

    @Query("SELECT * FROM transactions")
    fun getAllTransactions(): LiveData<List<Transactions>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertTransaction(transaction: Transactions)

    @Query("SELECT SUM(amount) FROM transactions")
    fun getTotalAmount(): LiveData<Double>

    @Query("SELECT SUM(amount) FROM transactions WHERE strftime('%m', date) = :month AND strftime('%Y', date) = :year")
    fun getTotalAmountForCurrentMonth(month: String, year: String): LiveData<Double>
}