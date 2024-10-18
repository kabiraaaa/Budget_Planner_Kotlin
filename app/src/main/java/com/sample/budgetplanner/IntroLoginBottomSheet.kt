package com.sample.budgetplanner

import android.app.Activity
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.gms.auth.api.Auth
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.sample.budgetplanner.AuthUtils.firebaseAuthWithGoogle
import com.sample.budgetplanner.databinding.BottomSheetIntroLoginBinding
import com.sample.budgetplanner.utils.DataStoreHelper
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch

class IntroLoginBottomSheet : BottomSheetDialogFragment(R.layout.bottom_sheet_intro_login) {

    private lateinit var binding: BottomSheetIntroLoginBinding

    // Use googleSignInResultLauncher for handling sign-in results
    private val googleSignInResultLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        val data = result.data
        if (result.resultCode == Activity.RESULT_OK && data != null) {
            val signInResult = Auth.GoogleSignInApi.getSignInResultFromIntent(data)
            if (signInResult?.isSuccess == true) {
                findNavController().navigate(R.id.action_introLoginBottomSheet_to_firebaseProfileUpdateFragment)
                val account = signInResult.signInAccount
                account?.let { firebaseAuthWithGoogle(requireActivity(), it) }
            } else {
                // Google Sign In Failed
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = BottomSheetIntroLoginBinding.bind(view)
        initClickListeners()
    }

    private fun initClickListeners() {
        binding.btnSkip.setOnClickListener {
            lifecycleScope.launch {
                // Check if the required DataStore values exist
                val goalsExist = DataStoreHelper.getUserGoals(requireContext()).firstOrNull()?.isNotEmpty() == true
                val salaryExists = DataStoreHelper.getMonthlySalary(requireContext()).firstOrNull() ?: 0.0 > 0.0
                val spendExists = DataStoreHelper.getMonthlySpend(requireContext()).firstOrNull() ?: 0.0 > 0.0

                val userName = DataStoreHelper.getUserName(requireContext()).firstOrNull()
                val userAge = DataStoreHelper.getUserAge(requireContext()).firstOrNull()
                val userGender = DataStoreHelper.getUserGender(requireContext()).firstOrNull()

                if (!goalsExist) {
                    // Navigate to goals fragment if goals are not set
                    findNavController().navigate(R.id.action_introLoginBottomSheet_to_introInvestmentFragment)
                } else if (!salaryExists || !spendExists) {
                    // Navigate to income fragment if salary or spend is not set
                    findNavController().navigate(R.id.action_introLoginBottomSheet_to_introBudgetFragment)
                } else if (!userName.isNullOrEmpty() && !userAge.isNullOrEmpty() && !userGender.isNullOrEmpty()) {
                    findNavController().navigate(R.id.action_introLoginBottomSheet_to_introIncomeFragment)
                }
                else {
                    // If all data exists, proceed to the next step
                    dismiss()
                    findNavController().navigate(R.id.action_introLoginBottomSheet_to_homeFragment)
                }
            }
        }

        binding.btnLoginGoogle.setOnClickListener {
            val signInIntent = AuthUtils.getGoogleSignInIntent(requireActivity())
            googleSignInResultLauncher.launch(signInIntent)
        }
    }
}