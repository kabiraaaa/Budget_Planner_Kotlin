package com.sample.budgetplanner

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
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
    private val TAG = "IntroLoginBottomSheet"

    // Use googleSignInResultLauncher for handling sign-in results
    private val googleSignInResultLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        val data = result.data
        if (result.resultCode == Activity.RESULT_OK && data != null) {
            val signInResult = Auth.GoogleSignInApi.getSignInResultFromIntent(data)
            if (signInResult?.isSuccess == true) {
                val account = signInResult.signInAccount
                account?.let {
                    firebaseAuthWithGoogle(requireActivity(), it)
                    checkDataStoreValuesAndNavigate()
                }
            } else {
                Log.e(TAG, "Google Sign-In Failed")
                Toast.makeText(requireContext(), "Google Sign-In Failed", Toast.LENGTH_SHORT).show()
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
            checkDataStoreValuesAndNavigate()
        }

        binding.btnLoginGoogle.setOnClickListener {
            val signInIntent = AuthUtils.getGoogleSignInIntent(requireActivity())
            googleSignInResultLauncher.launch(signInIntent)
        }
    }

    private fun checkDataStoreValuesAndNavigate() {
        lifecycleScope.launch {
            val dataStoreValues = checkDataStoreValues()

            Log.d(
                TAG,
                "DataStore values: $dataStoreValues"
            )

            when {
                dataStoreValues.userName.isNullOrEmpty() &&
                        dataStoreValues.userAge.isNullOrEmpty() &&
                        dataStoreValues.userGender.isNullOrEmpty() -> {
                    findNavController().navigate(R.id.action_introLoginBottomSheet_to_introIncomeFragment)
                }

                !dataStoreValues.goalsExist -> {
                    findNavController().navigate(R.id.action_introLoginBottomSheet_to_introInvestmentFragment)
                }

                !dataStoreValues.salaryExists || !dataStoreValues.spendExists -> {
                    findNavController().navigate(R.id.action_introLoginBottomSheet_to_introBudgetFragment)
                }

                else -> {
                    dismiss()
                    findNavController().navigate(R.id.action_introLoginBottomSheet_to_homeFragment)
                }
            }
        }
    }

    // Helper function to check DataStore values
    private suspend fun checkDataStoreValues(): DataStoreValues {
        val userName = DataStoreHelper.getUserName(requireContext()).firstOrNull()
        val userAge = DataStoreHelper.getUserAge(requireContext()).firstOrNull()
        val userGender = DataStoreHelper.getUserGender(requireContext()).firstOrNull()

        val goalsExist =
            DataStoreHelper.getUserGoals(requireContext()).firstOrNull()?.isNotEmpty() == true
        val salaryExists =
            DataStoreHelper.getMonthlySalary(requireContext()).firstOrNull() ?: 0.0 > 0.0
        val spendExists =
            DataStoreHelper.getMonthlySpend(requireContext()).firstOrNull() ?: 0.0 > 0.0

        return DataStoreValues(userName, userAge, userGender, goalsExist, salaryExists, spendExists)
    }

    // Data class to hold DataStore values
    data class DataStoreValues(
        val userName: String?,
        val userAge: String?,
        val userGender: String?,
        val goalsExist: Boolean,
        val salaryExists: Boolean,
        val spendExists: Boolean
    )
}