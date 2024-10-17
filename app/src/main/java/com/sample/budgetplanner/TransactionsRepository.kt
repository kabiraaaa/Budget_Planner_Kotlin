package com.sample.budgetplanner

import androidx.lifecycle.LiveData
import com.sample.budgetplanner.dao.TransactionsDao
import com.sample.budgetplanner.models.Transactions
import java.util.Calendar

class TransactionsRepository(private val transactionsDao: TransactionsDao) {

    fun getAllTransactions(): LiveData<List<Transactions>> = transactionsDao.getAllTransactions()

    suspend fun insertTransaction(transaction: Transactions) =
        transactionsDao.insertTransaction(transaction)

    fun getTotalAmount(): LiveData<Double> = transactionsDao.getTotalAmount()

    fun getTotalAmountForCurrentMonth(): LiveData<Double> {
        val calendar = Calendar.getInstance()
        val month = String.format("%02d", calendar.get(Calendar.MONTH) + 1) // Get month in MM format
        val year = calendar.get(Calendar.YEAR).toString()

        return transactionsDao.getTotalAmountForCurrentMonth(month, year)
    }
}
