package com.dscvit.cadence.ui.alarm

import android.content.Context
import android.content.SharedPreferences
import android.graphics.LinearGradient
import android.graphics.Shader
import android.os.Bundle
import android.text.format.DateFormat.is24HourFormat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import com.dscvit.cadence.R
import com.dscvit.cadence.databinding.FragmentAddAlarmBinding
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.*

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
        val isSystem24Hour = is24HourFormat(requireContext())
        val formatTime =
            SimpleDateFormat(if (isSystem24Hour) "hh:mm" else "h:mm a", Locale.getDefault())
        binding.apply {
            val time: String = formatTime.format(Date())
            digitalClock.text = time
            val paint = digitalClock.paint
            val width = paint.measureText(time)
            val textShader: Shader = LinearGradient(
                0f, 0f, width, digitalClock.textSize, intArrayOf(
                    requireContext().getColor(R.color.orange_light),
                    requireContext().getColor(R.color.pink_light),
                ), null, Shader.TileMode.CLAMP
            )
            digitalClock.paint.shader = textShader
        }

        binding.close.setOnClickListener {
            requireView().findNavController()
                .navigate(R.id.add_alarm_to_home)
        }

        binding.digitalClock.setOnClickListener {
            val picker =
                MaterialTimePicker.Builder()
                    .setTimeFormat(if (isSystem24Hour) TimeFormat.CLOCK_24H else TimeFormat.CLOCK_12H)
                    .build()
            picker.show(childFragmentManager, "fragment_tag")
            picker.addOnPositiveButtonClickListener {
                val simpleDateFormat = SimpleDateFormat("hh:mm", Locale.getDefault())
                val timeDate: Date = simpleDateFormat.parse("${picker.hour}:${picker.minute}")!!
                val time: String = formatTime.format(timeDate)
                binding.digitalClock.text = time
            }
        }
    }

}