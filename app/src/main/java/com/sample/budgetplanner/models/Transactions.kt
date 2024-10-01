package com.sample.budgetplanner.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.sql.Date
import java.sql.Time

@Entity(tableName = "transactions")
data class Transactions(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val name: String,
    val amount: Double,
    val category: String,
    val paymentMode: String,
    val date: String,
    val time: String,
    val note: String
)