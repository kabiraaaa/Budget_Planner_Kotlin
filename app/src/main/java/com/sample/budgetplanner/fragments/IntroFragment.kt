package com.sample.budgetplanner.fragments

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.sample.budgetplanner.R
import com.sample.budgetplanner.databinding.FragmentIntroBinding
import java.util.Currency
import java.util.Locale

class IntroFragment : Fragment(R.layout.fragment_intro) {

    private lateinit var binding: FragmentIntroBinding
    private val TAG = "IntroFragment"

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentIntroBinding.bind(view)

        binding.btnGetStarted.setOnClickListener {
            findNavController().navigate(R.id.introLoginBottomSheet)
        }


        val locale = Locale.getDefault()

        val currency = Currency.getInstance(locale)

        val currencyCode = currency.currencyCode
        val currencySymbol = currency.symbol

        Log.d(TAG,"currencyCode- $currencyCode")
        Log.d(TAG,"currencySymbol- $currencySymbol")
        Log.d(TAG,"locale- $locale")
        Log.d(TAG,"currency- $currency")
    }
}