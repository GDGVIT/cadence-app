package com.dscvit.cadence.ui.alarm

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import com.dscvit.cadence.R
import com.dscvit.cadence.databinding.FragmentAddAlarmBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddAlarmFragment : Fragment() {

    private val viewModel by activityViewModels<AddAlarmViewModel>()
    private var _binding: FragmentAddAlarmBinding? = null
    private val binding get() = _binding!!
    private lateinit var prefs: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.onBackPressedDispatcher?.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                requireView().findNavController()
                    .navigate(R.id.add_alarm_to_home)
            }
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddAlarmBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        prefs = requireContext().getSharedPreferences("user_data", Context.MODE_PRIVATE)

        binding.close.setOnClickListener {
//            requireActivity().onBackPressed()
            requireView().findNavController()
                .navigate(R.id.add_alarm_to_home)
        }
    }

}