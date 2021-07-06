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
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.transition.DrawableCrossFadeFactory
import com.dscvit.cadence.R
import com.dscvit.cadence.databinding.FragmentAddAlarmBinding
import com.dscvit.cadence.ui.home.HomeViewModel
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AddAlarmFragment : Fragment() {

    private val viewModel by activityViewModels<AddAlarmViewModel>()
    private val viewModelPlaylists by activityViewModels<HomeViewModel>()
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
        viewModel.setIs24Hr(is24HourFormat(requireContext()))
        binding.apply {
            val paint = digitalClock.paint
            val width = paint.measureText(viewModel.time.value)
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

        binding.nextBtn.setOnClickListener {
            GlobalScope.launch {
                viewModel.insertAlarm(
                    binding.alarmEditField.text.toString(),
                    listOf(true, true, true, true, true, true, true),
                    "ABC"
                )
            }
            requireView().findNavController()
                .navigate(R.id.add_alarm_to_home)
        }

        binding.digitalClock.apply {

            viewModel.time.observe(viewLifecycleOwner, { t ->
                text = t
            })

            setOnClickListener {
                val picker =
                    MaterialTimePicker.Builder()
                        .setTimeFormat(if (viewModel.is24hr.value == true) TimeFormat.CLOCK_24H else TimeFormat.CLOCK_12H)
                        .setHour((if (viewModel.hour.value != null) viewModel.hour.value else 0)!!)
                        .setMinute((if (viewModel.min.value != null) viewModel.min.value else 0)!!)
                        .build()
                picker.show(childFragmentManager, "fragment_tag")
                picker.addOnPositiveButtonClickListener {
                    viewModel.setTime(picker.hour, picker.minute, viewModel.is24hr.value == true)
                }
            }
        }

        viewModel.selectedPlaylist.observe(viewLifecycleOwner, { pn ->
            val playLen = viewModelPlaylists.spotifyRespPlay.value?.items?.size
            setPlaylistLayout(playLen, pn)
        })

        viewModelPlaylists.spotifyRespPlay.observe(viewLifecycleOwner, { playlist ->
            val playLen = playlist.items.size
            val pn = viewModel.selectedPlaylist.value
            if (pn != null) {
                setPlaylistLayout(playLen, pn)
            }
        })
    }

    private fun setPlaylistLayout(playLen: Int?, pn: Int) {
        var playNum = pn
        binding.apply {
            if (playLen != null && playLen > 0) {
                if (playNum > playLen || playNum < 0)
                    playNum = 0
                val currPlaylist =
                    viewModelPlaylists.spotifyRespPlay.value?.items?.get(playNum)
                if (currPlaylist != null) {
                    playlistName.text = currPlaylist.name
                    playlistArtist.text = "by ${currPlaylist.owner.display_name}"
                    val factory =
                        DrawableCrossFadeFactory.Builder().setCrossFadeEnabled(true).build()

                    Glide.with(playlistImage.context)
                        .load(currPlaylist.images[0].url)
                        .transition(DrawableTransitionOptions.withCrossFade(factory))
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .centerCrop()
                        .placeholder(R.drawable.profile_pic_placeholder)
                        .into(playlistImage)
                }
            }
        }
    }
}
