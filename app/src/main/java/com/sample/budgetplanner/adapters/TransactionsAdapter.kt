package com.xsdev.pdfreader.pdfeditor.pdf.document.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sample.budgetplanner.databinding.ItemTransactionsBinding
import com.sample.budgetplanner.models.Transactions

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

        fun bind(file: Transactions) {
            binding.tvTransactionName.text = file.name
            binding.tvTransactionAmount.text = file.amount.toString()
            binding.tvTransactionDate.text = "${file.date} | ${file.time}"
        }
    }
}