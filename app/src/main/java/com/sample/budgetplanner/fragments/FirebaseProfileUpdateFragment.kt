package com.sample.budgetplanner.fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.sample.budgetplanner.R
import com.sample.budgetplanner.databinding.FragmentFirebaseProfileUpdateBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class FirebaseProfileUpdateFragment : Fragment(R.layout.fragment_firebase_profile_update) {

    private lateinit var binding: FragmentFirebaseProfileUpdateBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentFirebaseProfileUpdateBinding.bind(view)

        binding.btnReqAccDeletion.setOnClickListener {
            val url = "https://forms.gle/hPbEJNXqFeRUoXVj9"
            val intent = Intent(Intent.ACTION_VIEW).apply {
                data = Uri.parse(url)
            }
            startActivity(intent)
        }

    }
}