package com.sample.budgetplanner.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.navigation.fragment.findNavController
import com.sample.budgetplanner.R
import com.sample.budgetplanner.databinding.FragmentIntroGoalBinding

class IntroGoalFragment : Fragment(R.layout.fragment_intro_goal) {

    private lateinit var binding: FragmentIntroGoalBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentIntroGoalBinding.bind(view)
        binding.btnGetStarted.setOnClickListener {
            findNavController().navigate(R.id.action_introInvestmentFragment_to_introBudgetFragment)
        }
    }
}