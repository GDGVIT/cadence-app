package com.dscvit.cadence.ui.login

import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.dscvit.cadence.R
import com.dscvit.cadence.databinding.FragmentConsentBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ConsentFragment : Fragment() {

    private var _binding: FragmentConsentBinding? = null
    private val binding get() = _binding!!
    private lateinit var prefs: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentConsentBinding.inflate(inflater, container, false)
        prefs = requireContext().getSharedPreferences("user_data", AppCompatActivity.MODE_PRIVATE)
        binding.nextBtn.setOnClickListener {
            prefs.edit().putBoolean("consent", true).apply()
            requireView().findNavController()
                .navigate(R.id.consent_to_home)
        }
        return binding.root
    }
}