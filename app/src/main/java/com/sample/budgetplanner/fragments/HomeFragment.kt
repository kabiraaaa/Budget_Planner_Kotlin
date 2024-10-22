package com.sample.budgetplanner.fragments

import android.app.Activity
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.sample.budgetplanner.AuthUtils
import com.sample.budgetplanner.AuthUtils.firebaseAuthWithGoogle
import com.sample.budgetplanner.FabAddExpenseBottomSheet
import com.sample.budgetplanner.MyApp
import com.sample.budgetplanner.R
import com.sample.budgetplanner.TransactionsRepository
import com.sample.budgetplanner.adapters.TransactionsAdapter
import com.sample.budgetplanner.databinding.FragmentHomeBinding
import com.sample.budgetplanner.models.Transactions
import com.sample.budgetplanner.utils.DataStoreHelper
import com.sample.budgetplanner.utils.DataStoreHelper.getMonthlySalary
import com.sample.budgetplanner.utils.DataStoreHelper.getMonthlySpend
import com.sample.budgetplanner.view_models.TransactionsViewModel
import com.sample.budgetplanner.view_models.TransactionsViewModelFactory
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

        initClickListeners()

        showProfileImage()
        setUserName()
        setUpTotalAmountAndMonth()
    }

    private fun initClickListeners() {
        binding.fabHomeAdd.setOnClickListener {
            val fabAddExpenseBottomSheet = FabAddExpenseBottomSheet(this)
            fabAddExpenseBottomSheet.show(
                requireActivity().supportFragmentManager,
                "ModalBottomSheet"
            )
        }
        binding.tvSignInPending.setOnClickListener {
            val signInIntent = AuthUtils.getGoogleSignInIntent(requireActivity())
            googleSignInResultLauncher.launch(signInIntent)
        }
    }

    private val googleSignInResultLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        val data = result.data
        if (result.resultCode == Activity.RESULT_OK && data != null) {
            val signInResult = Auth.GoogleSignInApi.getSignInResultFromIntent(data)
            if (signInResult?.isSuccess == true) {
                val account = signInResult.signInAccount
                account?.let {
                    // Call firebaseAuthWithGoogle and pass a callback to hide tvSignInPending
                    firebaseAuthWithGoogle(requireActivity(), it) {
                        // This will be called after Firebase auth is successfully completed
                        binding.tvSignInPending.visibility = View.GONE
                    }
                }
            } else {
                Log.e(TAG, "Google Sign-In Failed")
                Toast.makeText(requireContext(), "Google Sign-In Failed", Toast.LENGTH_SHORT).show()
            }
        }
    }


    private fun firebaseAuthWithGoogle(activity: FragmentActivity, account: GoogleSignInAccount, onSuccess: () -> Unit) {
        val credential = GoogleAuthProvider.getCredential(account.idToken, null)
        FirebaseAuth.getInstance().signInWithCredential(credential)
            .addOnCompleteListener(activity) { task ->
                if (task.isSuccessful) {
                    // Call the success callback once Firebase authentication is successful
                    onSuccess()
                } else {
                    Log.e(TAG, "Firebase Authentication Failed")
                    Toast.makeText(activity, "Firebase Authentication Failed", Toast.LENGTH_SHORT).show()
                }
            }
    }


    private fun setUpTotalAmountAndMonth() {
        binding.tvTotalAmountMonth.text =
            "Month: ${SimpleDateFormat("MMMM", Locale.getDefault()).format(Date())}"

        transactionViewModel.getTotalAmount().observe(viewLifecycleOwner) { totalAmount ->
            binding.tvTotalAmount.text =
                "${Currency.getInstance(Locale.getDefault()).symbol}${totalAmount ?: 0.0}"
        }
        observeUserFinanceData()
    }

    private fun observeUserFinanceData() {
        lifecycleScope.launch {
            val salary = getMonthlySalary(requireContext()).firstOrNull() ?: 0.0

            val formattedSalary = "${Currency.getInstance(Locale.getDefault()).symbol} $salary"
            binding.tvIncomingAmount.text = formattedSalary

            val spend = getMonthlySpend(requireContext()).firstOrNull() ?: 0.0

            // Observe the total transactions for the current month
            transactionViewModel.totalAmountForCurrentMonth.observe(viewLifecycleOwner) { totalAmount ->
                val transactionAmount = totalAmount ?: 0.0

                // Combine monthly spend and current month's transactions
                val combinedAmount =
                    "${Currency.getInstance(Locale.getDefault()).symbol} ${spend + transactionAmount}"
                binding.tvOutgoingAmount.text = combinedAmount

                // Calculate and display the balance
                val balance = salary - (spend + transactionAmount)
                binding.tvBalance.text =
                    "Balance: ${Currency.getInstance(Locale.getDefault()).symbol} $balance"
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun showProfileImage() {
        val user = FirebaseAuth.getInstance().currentUser
        val photoUrl = user?.photoUrl

        if (photoUrl != null) {
            Glide.with(this)
                .load(photoUrl)
                .into(binding.ivHomeProfile)
        }
        binding.tvGreetings.text = showGreetingBasedOnTime()
    }

    private fun setUserName() {
        // Launch coroutine to get DataStore value
        lifecycleScope.launch {
            // Collect the flow from DataStore
            val dataStoreUserName = DataStoreHelper.getUserName(requireActivity()).firstOrNull()

            // Get FirebaseAuth user
            val user = FirebaseAuth.getInstance().currentUser

            // Check if DataStore username is available
            val displayName = if (!dataStoreUserName.isNullOrEmpty()) {
                dataStoreUserName
            } else {
                // Fallback to FirebaseAuth displayName
                user?.displayName ?: "Guest"  // Fallback to "Guest" if both are null
            }

            // Set the TextView with the fetched name
            binding.tvUserName.text = displayName
        }
    }


    private fun initRecyclerView() {
        binding.rvRecentTransactions.layoutManager = LinearLayoutManager(requireActivity())
        val adapter = TransactionsAdapter(requireActivity())
        binding.rvRecentTransactions.adapter = adapter

        transactionViewModel.getAllTransactions().observe(viewLifecycleOwner) { transactions ->
            adapter.submitList(transactions)

            // Show or hide 'No transactions' TextView based on the list size
            if (transactions.isNullOrEmpty()) {
                binding.tvNoTransactionsAdded.visibility = View.VISIBLE
                binding.rvRecentTransactions.visibility = View.GONE
            } else {
                binding.tvNoTransactionsAdded.visibility = View.GONE
                binding.rvRecentTransactions.visibility = View.VISIBLE
            }
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

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "FirebaseUSer- ${FirebaseAuth.getInstance().currentUser}")
        if (FirebaseAuth.getInstance().currentUser == null) binding.tvSignInPending.visibility =
            View.VISIBLE
        else binding.tvSignInPending.visibility = View.GONE
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