package com.sample.budgetplanner

import android.app.Activity
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.navigation.fragment.findNavController
import com.google.android.gms.auth.api.Auth
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.sample.budgetplanner.AuthUtils.firebaseAuthWithGoogle
import com.sample.budgetplanner.databinding.BottomSheetIntroLoginBinding

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
            dismiss()
            findNavController().navigate(R.id.action_introLoginBottomSheet_to_introIncomeFragment)
        }

        binding.btnLoginGoogle.setOnClickListener {
            // Start Google Sign-In process and launch the result using the launcher
            val signInIntent = AuthUtils.getGoogleSignInIntent(requireActivity())
            googleSignInResultLauncher.launch(signInIntent)
        }
    }
}