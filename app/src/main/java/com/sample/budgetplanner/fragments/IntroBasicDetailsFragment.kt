package com.sample.budgetplanner.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.navigation.fragment.findNavController
import com.sample.budgetplanner.R
import com.sample.budgetplanner.databinding.FragmentIntroBasicDetailsBinding

class IntroBasicDetailsFragment : Fragment(R.layout.fragment_intro_basic_details) {

    private lateinit var binding: FragmentIntroBasicDetailsBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentIntroBasicDetailsBinding.bind(view)
        binding.btnIncomeContinue.setOnClickListener {
            findNavController().navigate(R.id.action_introIncomeFragment_to_introInvestmentFragment)
        }
    }
}