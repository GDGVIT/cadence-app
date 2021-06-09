package com.dscvit.cadence.ui.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import com.dscvit.cadence.R
import com.dscvit.cadence.databinding.FragmentLoginBinding

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
            Toast.makeText(context, "Clicked me", Toast.LENGTH_LONG).show()
        }
        viewModel.isSuccessful.observe(viewLifecycleOwner, { successful ->
            if(successful){
                Toast.makeText(context, "Changing page", Toast.LENGTH_LONG).show()
                requireView().findNavController().navigate(R.id.action_loginFragment_to_tracksListFragment)
            }
        })
        return binding.root
    }


}