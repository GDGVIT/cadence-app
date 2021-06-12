package com.dscvit.cadence.ui.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
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
        viewModel.isLoggedIn(false)
        binding.spotify.setOnClickListener {
            viewModel.isLoggedIn(true)
        }
        viewModel.isSuccessful.observe(viewLifecycleOwner, { successful ->
            if (successful) {
                Toast.makeText(context, "Changing page", Toast.LENGTH_SHORT).show()
                requireView().findNavController()
                    .navigate(R.id.action_loginFragment_to_tracksListFragment)
            }
        })
        return binding.root
    }


}