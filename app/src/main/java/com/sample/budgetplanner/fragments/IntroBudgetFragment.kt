package com.sample.budgetplanner.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.navigation.fragment.findNavController
import com.sample.budgetplanner.R
import com.sample.budgetplanner.databinding.FragmentIntroBudgetBinding
import com.sample.budgetplanner.utils.DataStoreHelper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Currency
import java.util.Locale

class IntroBudgetFragment : Fragment(R.layout.fragment_intro_budget) {

    private lateinit var binding: FragmentIntroBudgetBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentIntroBudgetBinding.bind(view)
        initCurrency()
        binding.btnIncomeContinue.setOnClickListener {
            CoroutineScope(Dispatchers.Default).launch {
                DataStoreHelper.saveOnBoardingStatus(requireContext(), true)
            }
            findNavController().navigate(R.id.action_introBudgetFragment_to_homeFragment)
        }
    }

    private fun initCurrency() {
        binding.tvCurrencyName.text = Currency.getInstance(Locale.getDefault()).displayName
        binding.tvCurrencySymbol.text = Currency.getInstance(Locale.getDefault()).symbol
        binding.tvSalaryCurrencySymbol.text = Currency.getInstance(Locale.getDefault()).symbol
    }
}