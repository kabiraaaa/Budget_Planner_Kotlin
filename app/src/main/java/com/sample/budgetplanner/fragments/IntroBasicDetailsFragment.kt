package com.sample.budgetplanner.fragments

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.chip.Chip
import com.google.firebase.auth.FirebaseAuth
import com.sample.budgetplanner.R
import com.sample.budgetplanner.databinding.FragmentIntroBasicDetailsBinding
import com.sample.budgetplanner.utils.DataStoreHelper
import com.sample.budgetplanner.utils.DataStoreHelper.saveUserAge
import com.sample.budgetplanner.utils.DataStoreHelper.saveUserGender
import com.sample.budgetplanner.utils.DataStoreHelper.saveUserName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch

class IntroBasicDetailsFragment : Fragment(R.layout.fragment_intro_basic_details) {

    private lateinit var binding: FragmentIntroBasicDetailsBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentIntroBasicDetailsBinding.bind(view)
        checkPreFilledUserDetails()
        initClickListeners()
    }

    private fun checkPreFilledUserDetails() {
        lifecycleScope.launch {
            val name = DataStoreHelper.getUserName(requireContext()).firstOrNull()
            val age = DataStoreHelper.getUserAge(requireContext()).firstOrNull()
            val gender = DataStoreHelper.getUserGender(requireContext()).firstOrNull()

            name?.let {
                if (it.isNotBlank()) binding.etFirstName.setText(it)
                else binding.etFirstName.setText(FirebaseAuth.getInstance().currentUser?.displayName)
            }

            age?.let { ageValue ->
                val chipId = findChipIdForAge(ageValue)
                if (chipId != -1) {
                    binding.chipGroupAge.check(chipId)
                }
            }

            gender?.let { genderValue ->
                val chipId = findChipIdForGender(genderValue)
                if (chipId != -1) {
                    binding.chipGroupGender.check(chipId)
                }
            }
        }
    }

    private fun findChipIdForAge(age: String): Int {
        return when (age) {
            getString(R.string.age_18_24) -> R.id.chip_age_18_24
            getString(R.string.age_25_34) -> R.id.chip_age_25_34
            getString(R.string.age_35_44) -> R.id.chip_age_35_44
            getString(R.string.age_45_54) -> R.id.chip_age_45_54
            getString(R.string.age_55_64) -> R.id.chip_age_55_64
            getString(R.string.age_65_plus) -> R.id.chip_age_65_plus
            else -> -1
        }
    }

    private fun findChipIdForGender(gender: String): Int {
        return when (gender) {
            getString(R.string.gender_male) -> R.id.chip_male
            getString(R.string.gender_female) -> R.id.chip_female
            getString(R.string.gender_na) -> R.id.chip_na
            else -> -1
        }
    }

    private fun initClickListeners() {
        binding.btnIncomeContinue.setOnClickListener {
            val isNameFilled = binding.etFirstName.text.isNotEmpty()
            val isGenderSelected = binding.chipGroupGender.checkedChipId != -1
            val isAgeSelected = binding.chipGroupAge.checkedChipId != -1

            if (isNameFilled && isGenderSelected && isAgeSelected) {
                saveUserDetails()
                navigateToNextScreen()
            } else if (isNameFilled || isGenderSelected || isAgeSelected) {
                saveAvailableDetails()
                showFillingAllDetailsToast()
                binding.btnIncomeSkip.visibility = View.VISIBLE
            } else {
                binding.btnIncomeSkip.visibility = View.VISIBLE
                showFillingAllDetailsToast()
            }
        }

        binding.btnIncomeSkip.setOnClickListener {
            findNavController().navigate(R.id.action_introIncomeFragment_to_introInvestmentFragment)
        }
    }

    private fun saveUserDetails() {
        CoroutineScope(Dispatchers.Default).launch {
            val selectedAge =
                requireActivity().findViewById<Chip>(binding.chipGroupAge.checkedChipId).text.toString()
            val selectedGender =
                requireActivity().findViewById<Chip>(binding.chipGroupGender.checkedChipId).text.toString()
            saveUserName(requireActivity(), binding.etFirstName.text.toString())
            saveUserAge(requireActivity(), selectedAge)
            saveUserGender(requireActivity(), selectedGender)
        }
    }

    private fun saveAvailableDetails() {
        CoroutineScope(Dispatchers.Default).launch {
            if (binding.etFirstName.text.isNotEmpty()) {
                saveUserName(requireActivity(), binding.etFirstName.text.toString())
            }
            if (binding.chipGroupAge.checkedChipId != -1) {
                val selectedAge =
                    requireActivity().findViewById<Chip>(binding.chipGroupAge.checkedChipId).text.toString()
                saveUserAge(requireActivity(), selectedAge)
            }
            if (binding.chipGroupGender.checkedChipId != -1) {
                val selectedGender =
                    requireActivity().findViewById<Chip>(binding.chipGroupGender.checkedChipId).text.toString()
                saveUserGender(requireActivity(), selectedGender)
            }
        }
    }

    private fun navigateToNextScreen() {
        CoroutineScope(Dispatchers.Main).launch {
            findNavController().navigate(R.id.action_introIncomeFragment_to_introInvestmentFragment)
        }
    }

    private fun showFillingAllDetailsToast() {
        Toast.makeText(
            requireActivity(),
            "We recommend you fill in all details for a better experience.",
            Toast.LENGTH_LONG
        ).show()
    }
}
