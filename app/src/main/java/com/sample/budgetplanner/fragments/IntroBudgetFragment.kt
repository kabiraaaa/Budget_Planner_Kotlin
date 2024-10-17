package com.sample.budgetplanner.fragments

import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.sample.budgetplanner.R
import com.sample.budgetplanner.databinding.FragmentIntroBudgetBinding
import com.sample.budgetplanner.utils.DataStoreHelper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import java.util.Currency
import java.util.Locale

class IntroBudgetFragment : Fragment(R.layout.fragment_intro_budget) {

    private lateinit var binding: FragmentIntroBudgetBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentIntroBudgetBinding.bind(view)

        initCurrency()

        // Pre-fill salary and spend if they exist in DataStore
        lifecycleScope.launch {
            val savedSalary = DataStoreHelper.getMonthlySalary(requireContext()).firstOrNull() ?: 0.0
            val savedSpend = DataStoreHelper.getMonthlySpend(requireContext()).firstOrNull() ?: 0.0

            if (savedSalary > 0.0) {
                binding.llSalary.findViewById<EditText>(R.id.etSalary).setText(savedSalary.toString())
            }
            if (savedSpend > 0.0) {
                binding.llSpends.findViewById<EditText>(R.id.etSpends).setText(savedSpend.toString())
            }
        }

        initClickListeners()
    }

    private fun initClickListeners() {
        binding.btnIncomeContinue.setOnClickListener {
            val salaryInput = binding.llSalary.findViewById<EditText>(R.id.etSalary).text.toString()
            val spendInput = binding.llSpends.findViewById<EditText>(R.id.etSpends).text.toString()

            if (salaryInput.isEmpty() && spendInput.isEmpty()) {
                // Show a toast message if no salary or spend is entered
                Toast.makeText(
                    requireContext(),
                    "Please enter your salary or spend, or skip",
                    Toast.LENGTH_SHORT
                ).show()
                // Make the skip button visible
                binding.btnGoalSkip.visibility = View.VISIBLE
            } else {
                // Parse the salary and spend inputs to double, using default values if empty
                val salary = salaryInput.toDoubleOrNull() ?: 0.0
                val spend = spendInput.toDoubleOrNull() ?: 0.0

                // Save the salary and spend to DataStore
                CoroutineScope(Dispatchers.Default).launch {
                    if (salary > 0.0) {
                        DataStoreHelper.saveMonthlySalary(requireContext(), salary)
                    }
                    if (spend > 0.0) {
                        DataStoreHelper.saveMonthlySpend(requireContext(), spend)
                    }
                    // Save onboarding status
                    DataStoreHelper.saveOnBoardingStatus(requireContext(), true)
                }

                // Navigate to the next fragment
                findNavController().navigate(R.id.action_introBudgetFragment_to_homeFragment)
            }
        }

        binding.btnGoalSkip.setOnClickListener {
            findNavController().navigate(R.id.action_introBudgetFragment_to_homeFragment)
        }
    }

    private fun initCurrency() {
        val currency = Currency.getInstance(Locale.getDefault())
        binding.tvCurrencyName.text = currency.displayName
        binding.tvCurrencySymbol.text = currency.symbol
        binding.tvSalaryCurrencySymbol2.text = currency.symbol
        binding.tvSalaryCurrencySymbol.text = currency.symbol
    }
}