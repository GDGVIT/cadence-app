package com.dscvit.cadence.ui.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import com.dscvit.cadence.R
import com.dscvit.cadence.databinding.FragmentLoginBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment : Fragment() {

    private val viewModel: LoginViewModel by activityViewModels()
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        binding.spotify.setOnClickListener {
            viewModel.isSyncing(true)
            viewModel.isLoggedIn(true)
        }
        viewModel.isSuccessful.observe(viewLifecycleOwner, { successful ->
            if (successful) {
                if (viewModel.isConsented.value == true) {
                    requireView().findNavController()
                        .navigate(R.id.login_to_home)
                } else {
                    requireView().findNavController()
                        .navigate(R.id.login_to_consent)
                }
            }
        })

        return binding.root
    }


}