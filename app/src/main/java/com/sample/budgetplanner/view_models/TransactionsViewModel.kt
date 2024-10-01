package com.sample.budgetplanner.view_models

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sample.budgetplanner.TransactionsRepository
import com.sample.budgetplanner.models.Transactions
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TransactionsViewModel(private val userRepository: TransactionsRepository) : ViewModel() {

    fun getAllTransactions(): LiveData<List<Transactions>> = userRepository.getAllTransactions()

    fun insertTransaction(transaction: Transactions) {
        viewModelScope.launch(Dispatchers.IO) {
            userRepository.insertTransaction(transaction)
        }
    }
}

