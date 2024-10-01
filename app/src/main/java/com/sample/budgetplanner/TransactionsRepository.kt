package com.sample.budgetplanner

import androidx.lifecycle.LiveData
import com.sample.budgetplanner.dao.TransactionsDao
import com.sample.budgetplanner.models.Transactions

class TransactionsRepository(private val transactionsDao: TransactionsDao) {

    fun getAllTransactions(): LiveData<List<Transactions>> = transactionsDao.getAllTransactions()

    suspend fun insertTransaction(transaction: Transactions) =
        transactionsDao.insertTransaction(transaction)
}
