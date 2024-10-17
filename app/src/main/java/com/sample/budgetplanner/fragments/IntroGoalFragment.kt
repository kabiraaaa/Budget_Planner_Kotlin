package com.sample.budgetplanner.fragments

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.sample.budgetplanner.R
import com.sample.budgetplanner.databinding.FragmentIntroGoalBinding
import com.sample.budgetplanner.utils.DataStoreHelper.getUserGoals
import com.sample.budgetplanner.utils.DataStoreHelper.saveUserGoals
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch

class IntroGoalFragment : Fragment(R.layout.fragment_intro_goal) {

    private lateinit var binding: FragmentIntroGoalBinding
    private val selectedGoalsSet = mutableSetOf<String>() // Set to store selected goals

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentIntroGoalBinding.bind(view)

        // Retrieve user goals from DataStore and preselect the grid items
        lifecycleScope.launch {
            val savedGoals = getUserGoals(requireContext()).firstOrNull() ?: emptySet()
            selectedGoalsSet.addAll(savedGoals) // Add saved goals to the selected set
            setupGrid() // Call setupGrid after retrieving saved goals
        }

        initClickListeners()
    }

    private fun initClickListeners() {
        binding.btnGoalSetStarted.setOnClickListener {
            if (selectedGoalsSet.isEmpty()) {
                // If no goals are selected, show a toast message and make the Skip button visible
                Toast.makeText(
                    requireContext(),
                    "Please select at least one goal or skip",
                    Toast.LENGTH_SHORT
                ).show()
                binding.btnGoalSkip.visibility = View.VISIBLE
            } else {
                // If goals are selected, save them to DataStore and navigate to the next fragment
                val selectedGoals = selectedGoalsSet.joinToString(", ")
                Log.d("SelectedGoals", selectedGoals)

                // Save the selected goals to DataStore
                lifecycleScope.launch {
                    saveUserGoals(requireContext(), selectedGoalsSet)
                }

                // Navigate to the next fragment
                findNavController().navigate(R.id.action_introInvestmentFragment_to_introBudgetFragment)
            }
        }
        binding.btnGoalSkip.setOnClickListener {
            findNavController().navigate(R.id.action_introInvestmentFragment_to_introBudgetFragment)
        }
    }

    private fun setupGrid() {
        for (i in 0 until binding.gridLayout.childCount) {
            val item = binding.gridLayout.getChildAt(i) as LinearLayout
            val textView = item.getChildAt(1) as TextView // Assuming TextView is the second child of the LinearLayout
            val goalText = textView.text.toString() // Get the goal text from the TextView

            // Check if this goal is already selected (exists in saved goals)
            if (selectedGoalsSet.contains(goalText)) {
                item.setBackgroundResource(R.drawable.ic_goal_selected) // Apply selected background
                item.isSelected = true
            }

            item.setOnClickListener {
                if (item.isSelected) {
                    item.setBackgroundResource(R.drawable.ic_goal_unselected) // Reset to unselected background
                    selectedGoalsSet.remove(goalText) // Remove from selected goals
                    item.isSelected = false
                } else {
                    item.setBackgroundResource(R.drawable.ic_goal_selected) // Apply selected background
                    selectedGoalsSet.add(goalText) // Add to selected goals
                    item.isSelected = true
                }
            }
        }
    }
}