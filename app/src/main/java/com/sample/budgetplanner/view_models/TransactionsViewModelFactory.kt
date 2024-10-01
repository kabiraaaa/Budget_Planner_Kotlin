package com.sample.budgetplanner.view_models

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.sample.budgetplanner.TransactionsRepository

class TransactionsViewModelFactory(
    private val repository: TransactionsRepository
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TransactionsViewModel::class.java)) {
            return TransactionsViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
