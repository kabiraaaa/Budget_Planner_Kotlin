package com.sample.budgetplanner.fragments

import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.sample.budgetplanner.FabAddExpenseBottomSheet
import com.sample.budgetplanner.MyApp
import com.sample.budgetplanner.R
import com.sample.budgetplanner.TransactionsRepository
import com.sample.budgetplanner.databinding.FragmentHomeBinding
import com.sample.budgetplanner.models.Transactions
import com.sample.budgetplanner.utils.DataStoreHelper.getMonthlySalary
import com.sample.budgetplanner.utils.DataStoreHelper.getMonthlySpend
import com.sample.budgetplanner.view_models.TransactionsViewModel
import com.sample.budgetplanner.view_models.TransactionsViewModelFactory
import com.sample.budgetplanner.adapters.TransactionsAdapter
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.time.LocalTime
import java.util.Currency
import java.util.Date
import java.util.Locale

class HomeFragment : Fragment(R.layout.fragment_home), FabAddExpenseBottomSheet.AddExpense {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var transactionViewModel: TransactionsViewModel
    private val TAG = "HomeFragment"

    @RequiresApi(Build.VERSION_CODES.O)
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

        showProfileImageAndName()
        setUpTotalAmountAndMonth()
    }

    private fun setUpTotalAmountAndMonth() {
        binding.tvTotalAmountMonth.text =
            "Month: ${SimpleDateFormat("MMMM", Locale.getDefault()).format(Date())}"

        transactionViewModel.getTotalAmount().observe(viewLifecycleOwner) { totalAmount ->
            binding.tvTotalAmount.text = "${Currency.getInstance(Locale.getDefault()).symbol} ${totalAmount ?: 0.0}"
        }
        observeUserFinanceData()
    }

    private fun observeUserFinanceData() {
        // Observe the user's monthly salary from DataStore and display it
        lifecycleScope.launch {
            // Collect the monthly salary from DataStore
            val salary = getMonthlySalary(requireContext()).firstOrNull() ?: 0.0

            // Display the monthly salary with currency symbol
            val formattedSalary = "${Currency.getInstance(Locale.getDefault()).symbol} $salary"
            binding.tvIncomingAmount.text = formattedSalary

            // Get the monthly spend from DataStore
            val spend = getMonthlySpend(requireContext()).firstOrNull() ?: 0.0

            // Observe the total transactions for the current month
            transactionViewModel.totalAmountForCurrentMonth.observe(viewLifecycleOwner) { totalAmount ->
                val transactionAmount = totalAmount ?: 0.0

                // Combine monthly spend and current month's transactions
                val combinedAmount =
                    "${Currency.getInstance(Locale.getDefault()).symbol} ${spend + transactionAmount}"

                // Display the combined outgoing amount in the TextView
                binding.tvOutgoingAmount.text = combinedAmount

                // Calculate and display the balance
                val balance = salary - (spend + transactionAmount)
                binding.tvBalance.text = "Balance: ${Currency.getInstance(Locale.getDefault()).symbol} $balance"
            }
        }


    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun showProfileImageAndName() {
        val user = FirebaseAuth.getInstance().currentUser
        val photoUrl = user?.photoUrl

        if (photoUrl != null) {
            Glide.with(this)
                .load(photoUrl)
                .into(binding.ivHomeProfile)
        }
        binding.tvGreetings.text = showGreetingBasedOnTime()
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

    @RequiresApi(Build.VERSION_CODES.O)
    fun showGreetingBasedOnTime(): String {
        val currentTime = LocalTime.now()
        return when (currentTime.hour) {
            in 0..11 -> "Good Morning!"
            in 12..16 -> "Good Afternoon!"
            in 17..20 -> "Good Evening!"
            else -> "Good Night!"
        }
    }
}
