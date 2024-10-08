package com.sample.budgetplanner

import android.os.Bundle
import android.view.View
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.sample.budgetplanner.databinding.BottomSheetFabAddExpenseBinding
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class FabAddExpenseBottomSheet(private val listener: AddExpense) :
    BottomSheetDialogFragment(R.layout.bottom_sheet_fab_add_expense) {

    private lateinit var binding: BottomSheetFabAddExpenseBinding
    private val currentDate: String =
        SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Calendar.getInstance().time)
    private val currentTime: String =
        SimpleDateFormat("HH:mm", Locale.getDefault()).format(Calendar.getInstance().time)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = BottomSheetFabAddExpenseBinding.bind(view)
        binding.tvDate.text = currentDate
        binding.tvTime.text = currentTime
        initClickListeners()
    }

    private fun initClickListeners() {
        binding.btnIncomeContinue.setOnClickListener {
            val name = binding.etName.text.toString()
            val amount = binding.etAmount.text.toString()
            val paymentMode = binding.etPaymentMode.text.toString()
            val category = binding.etCategory.text.toString()
            val notes = binding.etNotes.text.toString()

            listener.onExpenseAdded(
                name,
                amount.toDouble(),
                paymentMode,
                category,
                notes,
                currentDate,
                currentTime
            )
            dismiss()
        }
    }

    interface AddExpense {
        fun onExpenseAdded(
            name: String,
            amount: Double,
            paymentMode: String,
            category: String,
            notes: String,
            currentDate: String,
            currentTime: String
        )
    }
}