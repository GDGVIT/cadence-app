package com.dscvit.cadence.ui.alarm

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.LinearGradient
import android.graphics.Shader
import android.os.Bundle
import android.text.format.DateFormat.is24HourFormat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.content.res.AppCompatResources
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.transition.DrawableCrossFadeFactory
import com.dscvit.cadence.R
import com.dscvit.cadence.alarm.AlarmReceiver
import com.dscvit.cadence.databinding.FragmentAddAlarmBinding
import com.dscvit.cadence.model.song.TracksData
import com.dscvit.cadence.ui.home.HomeViewModel
import com.dscvit.cadence.util.Constants.ALARM_INSET_COMPLETE
import com.dscvit.cadence.util.Constants.CONTINUE_PRESSED
import com.dscvit.cadence.util.Constants.NOT_STARTED
import com.dscvit.cadence.util.Constants.NO_INTERNET
import com.dscvit.cadence.util.Constants.PLAYLIST_FAILED
import com.dscvit.cadence.util.Constants.SPOTIFY_FAILED
import com.dscvit.cadence.util.Constants.STARTED
import com.dscvit.cadence.util.Constants.TRACK_FETCH_COMPLETE
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import java.util.Calendar

@AndroidEntryPoint
class AddAlarmFragment : Fragment() {

    private val viewModel by activityViewModels<AddAlarmViewModel>()
    private val viewModelPlaylists by activityViewModels<HomeViewModel>()
    private var _binding: FragmentAddAlarmBinding? = null
    private val binding get() = _binding!!
    private lateinit var prefs: SharedPreferences
    private var recList = emptyList<Boolean>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.onBackPressedDispatcher?.addCallback(
            this,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    if (viewModel.alarmInserted.value == 0)
                        requireView().findNavController()
                            .navigate(R.id.add_alarm_to_home)
                }
            }
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddAlarmBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        prefs = requireContext().getSharedPreferences("user_data", Context.MODE_PRIVATE)
        var token = prefs.getString("token", "").toString()
        viewModelPlaylists.token.observe(
            viewLifecycleOwner,
            { t ->
                token = t
            }
        )
        viewModel.setIs24Hr(is24HourFormat(requireContext()))
        viewModel.setAlarmInserted(NOT_STARTED)
        viewModel.setAlarmId(-1)
        binding.apply {
            val paint = digitalClock.paint
            val width = paint.measureText(viewModel.time.value)
            val textShader: Shader = LinearGradient(
                0f, 0f, width, digitalClock.textSize,
                intArrayOf(
                    requireContext().getColor(R.color.orange_light),
                    requireContext().getColor(R.color.pink_light),
                ),
                null, Shader.TileMode.CLAMP
            )
            digitalClock.paint.shader = textShader
        }

        binding.close.setOnClickListener {
            requireView().findNavController()
                .navigate(R.id.add_alarm_to_home)
        }

        val v: View = LayoutInflater
            .from(context)
            .inflate(R.layout.dialog_song, null)

        val dialog = MaterialAlertDialogBuilder(requireContext())
            .setView(v)
            .setBackground(
                AppCompatResources.getDrawable(
                    requireContext(),
                    R.color.transparent
                )
            )
            .create()

        dialog.setCanceledOnTouchOutside(false)
        dialog.setCancelable(false)

        val continueBtn = v.findViewById<Button>(R.id.continue_btn)
        val loadingLayout = v.findViewById<LinearLayout>(R.id.loading)
        val songArt = v.findViewById<ImageView>(R.id.songArt)
        val errorImageView = v.findViewById<ImageView>(R.id.error_image)
        val songName = v.findViewById<TextView>(R.id.songName)
        val songArtist = v.findViewById<TextView>(R.id.artistName)
        val error = v.findViewById<TextView>(R.id.error)
        val errorDesc = v.findViewById<TextView>(R.id.error_description)
        val progressBar = v.findViewById<ProgressBar>(R.id.progressBar)

        continueBtn.setOnClickListener {
            if (viewModel.alarmInserted.value != PLAYLIST_FAILED)
                viewModel.setAlarmInserted(CONTINUE_PRESSED)
            dialog.dismiss()
        }

        binding.nextBtn.setOnClickListener {
            if (binding.alarmEditField.text.toString().trim() != "") {
                viewModel.setAlarmInserted(STARTED)
                recList = listOf(
                    binding.toggleSun.isChecked,
                    binding.toggleMon.isChecked,
                    binding.toggleTue.isChecked,
                    binding.toggleWed.isChecked,
                    binding.toggleThu.isChecked,
                    binding.toggleFri.isChecked,
                    binding.toggleSat.isChecked,
                )
                viewModel.getSongData(
                    binding.alarmEditField.text.toString(),
                    recList,
                    token
                )
                dialog.show()
            } else {
                binding.alarmTextField.error = "Required"
            }
        }

        viewModel.alarmInserted.observe(
            viewLifecycleOwner,
            { isInserted ->
                when (isInserted) {
                    CONTINUE_PRESSED -> {
                        val id = viewModel.alarmId.value!!
                        if (id >= 0) {
                            val alarmManager =
                                requireContext().getSystemService(Context.ALARM_SERVICE) as AlarmManager
                            val i = Intent(context, AlarmReceiver::class.java)
                            i.putExtra("ALARM_ID", id)
                            val pi = PendingIntent.getBroadcast(context, id.toInt(), i, 0)

                            val now = Calendar.getInstance()
                            val schedule = now.clone() as Calendar
                            schedule[Calendar.HOUR_OF_DAY] = viewModel.hour.value!!
                            schedule[Calendar.MINUTE] = viewModel.min.value!!
                            schedule[Calendar.SECOND] = 0
                            schedule[Calendar.MILLISECOND] = 0

                            if (!recList.contains(true)) {
                                if (schedule <= now) schedule.add(Calendar.DATE, 1)
                                setAlarm(pi, schedule, alarmManager)
                            } else {
                                Timber.d("Weekday: ${now[Calendar.DAY_OF_WEEK]}, ${Calendar.SUNDAY}")
                                var alarmSet = false

                                for (idx in now[Calendar.DAY_OF_WEEK] - 1..now[Calendar.DAY_OF_WEEK] + 5) {
                                    val idx2 = idx % 7 + 1
                                    Timber.d("werk: $idx2, ${recList[idx2 - 1]}")
                                    if (recList[idx2 - 1]) {
                                        if (schedule > now) {
                                            Timber.d("Next Alarm: $idx2, ${schedule[Calendar.DAY_OF_WEEK]}")
                                            setAlarm(pi, schedule, alarmManager)
                                            alarmSet = true
                                            break
                                        }
                                    }
                                    schedule.add(Calendar.DATE, 1)
                                }
                                if (!alarmSet) {
                                    setAlarm(pi, schedule, alarmManager)
                                }
                            }
                        }
                        viewModel.setAlarmId(-1)
                        viewModel.setAlarmInserted(NOT_STARTED)
                        requireView().findNavController().navigate(R.id.add_alarm_to_home)
                    }
                    ALARM_INSET_COMPLETE -> {
                        progressBar.setProgress(100, true)
                        loadingLayout.visibility = View.GONE
                        setSongLayout(
                            viewModel.trackData.value!!,
                            songName,
                            songArtist,
                            songArt,
                            continueBtn
                        )
                    }
                    TRACK_FETCH_COMPLETE -> {
                        progressBar.setProgress(90, true)
                    }
                    STARTED -> {
                        progressBar.setProgress(85, true)
                        continueBtn.text = "Continue"
                    }
                    NOT_STARTED -> {
                        progressBar.setProgress(0, true)
                    }
                    PLAYLIST_FAILED -> {
                        continueBtn.isEnabled = true
                        progressBar.visibility = View.GONE
                        errorImageView.visibility = View.VISIBLE
                        error.text = "Incompatible Playlist"
                        errorDesc.text = "Please use some other playlist"
                        continueBtn.text = "Change Playlist"
                        viewModel.setAlarmInserted(NOT_STARTED)
                    }
                    SPOTIFY_FAILED -> {
                        continueBtn.isEnabled = true
                        progressBar.visibility = View.GONE
                        errorImageView.visibility = View.VISIBLE
                        loadingLayout.visibility = View.VISIBLE
                        error.text = "Fetch Failed!"
                        errorDesc.text = "Spotify didn't send the song data"
                    }
                    NO_INTERNET -> {
                        continueBtn.isEnabled = true
                        progressBar.visibility = View.GONE
                        errorImageView.visibility = View.VISIBLE
                        loadingLayout.visibility = View.VISIBLE
                        error.text = "Fetch Failed!"
                        errorDesc.text = "Couldn't connect to the internet"
                    }
                }
            }
        )

        binding.digitalClock.apply {

            viewModel.time.observe(
                viewLifecycleOwner,
                { t ->
                    text = t
                }
            )

            setOnClickListener {
                val picker =
                    MaterialTimePicker.Builder()
                        .setTimeFormat(
                            if (viewModel.is24hr.value == true) TimeFormat.CLOCK_24H
                            else TimeFormat.CLOCK_12H
                        )
                        .setHour((if (viewModel.hour.value != null) viewModel.hour.value else 0)!!)
                        .setMinute((if (viewModel.min.value != null) viewModel.min.value else 0)!!)
                        .build()
                picker.show(childFragmentManager, "fragment_tag")
                picker.addOnPositiveButtonClickListener {
                    viewModel.setTime(
                        picker.hour,
                        picker.minute,
                        viewModel.is24hr.value == true
                    )
                }
            }
        }

        binding.changeBtn.setOnClickListener {
            requireView().findNavController()
                .navigate(R.id.add_alarm_to_playlist)
        }

        viewModel.selectedPlaylist.observe(
            viewLifecycleOwner,
            { pn ->
                val playLen = viewModelPlaylists.spotifyRespPlay.value?.items?.size
                setPlaylistLayout(playLen, pn)
            }
        )

        viewModelPlaylists.spotifyRespPlay.observe(
            viewLifecycleOwner,
            { playlist ->
                val playLen = playlist.items.size
                val pn = viewModel.selectedPlaylist.value
                if (pn != null) {
                    setPlaylistLayout(playLen, pn)
                }
            }
        )
    }

    private fun setSongLayout(
        trackData: TracksData,
        songName: TextView,
        songArtist: TextView,
        songArt: ImageView,
        continueBtn: Button
    ) {
        val factory = DrawableCrossFadeFactory.Builder().setCrossFadeEnabled(true).build()
        songName.text = trackData.tracks[0].name
        songArtist.text = trackData.tracks[0].artists[0].name
        Glide.with(songArt.context)
            .load(trackData.tracks[0].album.images[0].url)
            .transition(DrawableTransitionOptions.withCrossFade(factory))
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .centerCrop()
            .into(songArt)
        continueBtn.isEnabled = true
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
                    viewModel.setPlaylistId(currPlaylist.id)
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

    private fun setAlarm(pi: PendingIntent, schedule: Calendar, alarmManager: AlarmManager) {
        val info = AlarmManager.AlarmClockInfo(schedule.timeInMillis, pi)
        alarmManager.setAlarmClock(info, pi)
        Toast.makeText(
            context,
            "Alarm scheduled for ${viewModel.time.value}",
            Toast.LENGTH_LONG
        ).show()
    }
}
