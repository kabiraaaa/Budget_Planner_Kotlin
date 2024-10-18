package com.sample.budgetplanner.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sample.budgetplanner.databinding.ItemTransactionsBinding
import com.sample.budgetplanner.models.Transactions
import java.util.Currency
import java.util.Locale

class TransactionsAdapter(private val context: Context) :
    RecyclerView.Adapter<TransactionsAdapter.FileItemViewHolder>() {

    private var transactions: List<Transactions> = listOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FileItemViewHolder {
        val binding = ItemTransactionsBinding.inflate(LayoutInflater.from(context), parent, false)
        return FileItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FileItemViewHolder, position: Int) {
        val fileList = transactions[position]
        holder.bind(fileList)
    }

    override fun getItemCount(): Int {
        return transactions.size
    }

    fun submitList(allTransactions: List<Transactions>) {
        transactions = allTransactions
        notifyDataSetChanged()
    }


    class FileItemViewHolder(private val binding: ItemTransactionsBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(transactions: Transactions) {
            binding.tvTransactionName.text = transactions.name
            binding.tvTransactionAmount.text =
                "${Currency.getInstance(Locale.getDefault()).symbol} ${transactions.amount}"
            binding.tvTransactionDate.text = "${transactions.date} | ${transactions.time}"
        }
    }
}