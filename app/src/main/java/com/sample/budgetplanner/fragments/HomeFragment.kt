package com.sample.budgetplanner.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.sample.budgetplanner.FabAddExpenseBottomSheet
import com.sample.budgetplanner.MyApp
import com.sample.budgetplanner.R
import com.sample.budgetplanner.TransactionsRepository
import com.sample.budgetplanner.databinding.FragmentHomeBinding
import com.sample.budgetplanner.models.Transactions
import com.sample.budgetplanner.view_models.TransactionsViewModel
import com.sample.budgetplanner.view_models.TransactionsViewModelFactory
import com.xsdev.pdfreader.pdfeditor.pdf.document.adapters.TransactionsAdapter
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class HomeFragment : Fragment(R.layout.fragment_home), FabAddExpenseBottomSheet.AddExpense {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var transactionViewModel: TransactionsViewModel
    private val TAG = "HomeFragment"

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentHomeBinding.bind(view)

        val transactionsDao = MyApp.database.transactionDao()
        val repository = TransactionsRepository(transactionsDao)
        val factory = TransactionsViewModelFactory(repository)
        transactionViewModel = ViewModelProvider(this, factory)[TransactionsViewModel::class.java]

        initRecyclerView()

        binding.fabHomeAdd.setOnClickListener {
            val fabAddExpenseBottomSheet = FabAddExpenseBottomSheet(this)
            fabAddExpenseBottomSheet.show(
                requireActivity().supportFragmentManager,
                "ModalBottomSheet"
            )
        }

        binding.tvTotalAmountMonth.text =
            "Month: ${SimpleDateFormat("MMMM", Locale.getDefault()).format(Date())}"

        showProfileImageAndName()
    }

    private fun showProfileImageAndName() {
        val user = FirebaseAuth.getInstance().currentUser
        val photoUrl = user?.photoUrl

        if (photoUrl != null) {
            Glide.with(this)
                .load(photoUrl)
                .into(binding.ivHomeProfile)
        }

        binding.tvUserName.text = user?.displayName
    }

    private fun initRecyclerView() {
        binding.rvRecentTransactions.layoutManager = LinearLayoutManager(requireActivity())
        val adapter = TransactionsAdapter(requireActivity())
        binding.rvRecentTransactions.adapter = adapter
        transactionViewModel.getAllTransactions().observe(viewLifecycleOwner) { transactions ->
            adapter.submitList(transactions)
        }
    }

    override fun onExpenseAdded(
        name: String,
        amount: Double,
        paymentMode: String,
        category: String,
        notes: String,
        currentDate: String,
        currentTime: String
    ) {
        transactionViewModel.insertTransaction(
            Transactions(0, name, amount, category, paymentMode, currentDate, currentTime, notes)
        )
    }
}
