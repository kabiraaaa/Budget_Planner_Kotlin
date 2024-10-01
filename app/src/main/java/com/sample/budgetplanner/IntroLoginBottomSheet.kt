package com.sample.budgetplanner

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.sample.budgetplanner.databinding.BottomSheetIntroLoginBinding

class IntroLoginBottomSheet : BottomSheetDialogFragment(R.layout.bottom_sheet_intro_login) {

    private lateinit var binding: BottomSheetIntroLoginBinding

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
    }

}