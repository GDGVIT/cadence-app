package com.dscvit.cadence.ui.home

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.Context.POWER_SERVICE
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.PowerManager
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.content.res.AppCompatResources
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade
import com.bumptech.glide.request.transition.DrawableCrossFadeFactory
import com.dscvit.cadence.R
import com.dscvit.cadence.adapter.AlarmAdapter
import com.dscvit.cadence.adapter.PlaylistAdapter
import com.dscvit.cadence.alarm.AlarmReceiver
import com.dscvit.cadence.databinding.FragmentHomeBinding
import com.dscvit.cadence.model.alarm.Alarm
import com.dscvit.cadence.util.OnEditAlarmListener
import com.dscvit.cadence.util.SpotifyConstants.CLIENT_ID
import com.dscvit.cadence.util.SpotifyConstants.REDIRECT_URI
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.spotify.android.appremote.api.ConnectionParams
import com.spotify.android.appremote.api.Connector
import com.spotify.android.appremote.api.SpotifyAppRemote
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import java.util.Calendar

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private val viewModel by activityViewModels<HomeViewModel>()
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var prefs: SharedPreferences
    private lateinit var playlistAdapter: PlaylistAdapter
    lateinit var token: String
    lateinit var refToken: String
    lateinit var imageUrl: String
    lateinit var alarmAdapter: AlarmAdapter
    var firstTime = true
    var firstTimeAlarm = true

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.bottomNav.setOnNavigationItemSelectedListener { item ->
            viewModel.setPage(item.itemId)
            true
        }

        viewModel.page.observe(
            viewLifecycleOwner,
            { p ->
                when (p) {
                    R.id.playlists -> {
                        binding.apply {
                            titleBar.text = context?.getString(R.string.playlists) ?: "Playlists"
                            alarmLayout.visibility = View.GONE
                            playlistsLayout.visibility = View.VISIBLE
                            addAlarm.hide()
                            syncPlaylist.show()
                        }
                    }
                    else -> {
                        binding.apply {
                            titleBar.text = context?.getString(R.string.alarms) ?: "Alarms"
                            playlistsLayout.visibility = View.GONE
                            alarmLayout.visibility = View.VISIBLE
                            addAlarm.show()
                            syncPlaylist.hide()
                        }
                    }
                }
            }
        )

        prefs = requireContext().getSharedPreferences("user_data", MODE_PRIVATE)
        refToken = prefs.getString("refresh_token", "").toString()
        imageUrl = prefs.getString("imageUrl", "").toString()
        val factory = DrawableCrossFadeFactory.Builder().setCrossFadeEnabled(true).build()
        Glide.with(requireContext())
            .load(imageUrl)
            .transition(withCrossFade(factory))
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .centerCrop()
            .placeholder(R.drawable.profile_pic_placeholder)
            .into(binding.profilePic)

        binding.syncPlaylist.hide()

        binding.syncPlaylist.setOnClickListener {
            binding.loadingPlaylists.show()
            viewModel.isSyncing(true)
        }

        binding.addAlarm.setOnClickListener {
            requireView().findNavController()
                .navigate(R.id.home_to_add_alarm)
        }

        viewModel.getAllAlarms()

        viewModel.alarmsList.observe(
            viewLifecycleOwner,
            { t ->
                if (t != null) {
                    if (t.isNotEmpty()) {
                        binding.noAlarms.visibility = View.GONE
                        if (firstTimeAlarm) {
                            alarmAdapter = AlarmAdapter(
                                t,
                                object : OnEditAlarmListener {
                                    override fun onToggle(alarm: Alarm) {
                                        viewModel.updateAlarm(alarm)
                                        if (alarm.isOn)
                                            setAlarm(alarm)
                                        else
                                            cancelAlarm(alarm)
                                    }

                                    override fun onDelete(alarm: Alarm) {
                                        val v: View = LayoutInflater
                                            .from(context)
                                            .inflate(R.layout.dialog_alarm_options, null)

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

                                        val deleteBtn = v.findViewById<Button>(R.id.delete_btn)
                                        val cancelBtn = v.findViewById<Button>(R.id.cancel_btn)

                                        deleteBtn.setOnClickListener {
                                            cancelAlarm(alarm)
                                            viewModel.deleteAlarm(alarm.id!!)
                                            dialog.dismiss()
                                        }

                                        cancelBtn.setOnClickListener {
                                            dialog.dismiss()
                                        }
                                        dialog.show()
                                    }
                                }
                            )
                            binding.alarms.apply {
                                layoutManager = LinearLayoutManager(context)
                                setHasFixedSize(true)
                                adapter = alarmAdapter
                            }
                            firstTimeAlarm = false
                        } else {
                            alarmAdapter.dataSetChange(t)
                            alarmAdapter.notifyDataSetChanged()
                        }
                    } else {
                        binding.noAlarms.visibility = View.VISIBLE
                        if (!firstTimeAlarm) {
                            alarmAdapter.dataSetChange(t)
                            alarmAdapter.notifyDataSetChanged()
                        }
                    }
                }
            }
        )

        viewModel.token.observe(
            viewLifecycleOwner,
            { t ->
                if (t != "" && t != null) {
                    token = t
                    prefs.edit().putString("token", token).apply()
                }
            }
        )

        try {
            viewModel.isSuccessful.observe(
                viewLifecycleOwner,
                { successful ->
                    if (successful) {
                        refToken = prefs.getString("refresh_token", "").toString()
                        if (viewModel.spotifyAppRemote.value != null) {
                            viewModel.setRefreshToken(refToken)
                        }
                    }
                }
            )

            viewModel.spotifyResp.observe(
                viewLifecycleOwner,
                { result ->
                    prefs.edit().apply {
                        putString("id", result.id)
                        putString("name", result.display_name)
                        try {
                            putString("imageUrl", result.images[0].url)
                        } catch (indexOutOfBoundsException: IndexOutOfBoundsException) {
                            putString("imageUrl", "")
                        }
                        putString("email", result.email)
                        apply()
                    }

                    try {
                        if (imageUrl != result.images[0].url) {
                            imageUrl = result.images[0].url
                            Glide.with(requireContext())
                                .load(imageUrl)
                                .transition(withCrossFade(factory))
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .centerCrop()
                                .placeholder(R.drawable.profile_pic_placeholder)
                                .into(binding.profilePic)
                        }
                    } catch (indexOutOfBoundsException: IndexOutOfBoundsException) {
                        imageUrl = ""
                    }
                }
            )

            val connectionParams = ConnectionParams.Builder(CLIENT_ID)
                .setRedirectUri(REDIRECT_URI)
                .showAuthView(true)
                .build()

            SpotifyAppRemote.connect(
                context,
                connectionParams,
                object : Connector.ConnectionListener {
                    override fun onConnected(appRemote: SpotifyAppRemote) {
                        viewModel.spotifyAppRemote(appRemote)
                        Timber.d("Connected! Yay!")
                        viewModel.setRefreshToken(refToken)
                    }

                    override fun onFailure(throwable: Throwable) {
                        Toast.makeText(context, "Unable to connect with Spotify", Toast.LENGTH_LONG)
                            .show()
                        Timber.e(throwable)
                        // Something went wrong when attempting to connect! Handle errors here
                    }
                }
            )

            viewModel.spotifyRespPlay.observe(
                viewLifecycleOwner,
                { result ->
                    binding.loadingPlaylists.hide()
                    if (firstTime) {
                        playlistAdapter =
                            PlaylistAdapter(result.items, viewModel.spotifyAppRemote.value!!)
                        binding.playlists.apply {
                            layoutManager = LinearLayoutManager(context)
                            setHasFixedSize(true)
                            adapter = playlistAdapter
                        }
                        firstTime = false
                    } else {
                        playlistAdapter.dataSetChange(result.items)
                        playlistAdapter.notifyDataSetChanged()
                    }
                }
            )
        } catch (e: Exception) {
            Toast.makeText(context, "Something went wrong :(", Toast.LENGTH_SHORT).show()
        }

        binding.profilePic.setOnClickListener {
            Toast.makeText(context, "Wanna logout? Nub", Toast.LENGTH_LONG).show()
        }
    }

    override fun onStart() {
        super.onStart()
        val packageName: String = requireContext().packageName
        val pm = requireContext().getSystemService(POWER_SERVICE) as PowerManager?
        if (!pm!!.isIgnoringBatteryOptimizations(packageName)) {
            val pmIntent = Intent()
            pmIntent.action = Settings.ACTION_IGNORE_BATTERY_OPTIMIZATION_SETTINGS
            Toast.makeText(
                context,
                "Please turn off the Battery Optimization Settings for Cadence to ring the alarms on time.",
                Toast.LENGTH_LONG
            ).show()
            startActivity(pmIntent)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.spotifyAppRemote.value.let {
            SpotifyAppRemote.disconnect(it)
        }
    }

    private fun setAlarm(alarm: Alarm) {
        val alarmManager =
            requireContext().getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val pi = getPendingIntent(alarm)

        val now = Calendar.getInstance()
        val schedule = now.clone() as Calendar
        schedule[Calendar.HOUR_OF_DAY] = alarm.hour
        schedule[Calendar.MINUTE] = alarm.minute
        schedule[Calendar.SECOND] = 0
        schedule[Calendar.MILLISECOND] = 0

        if (!alarm.isRepeating) {
            if (schedule <= now) schedule.add(Calendar.DATE, 1)
            setAlarmManager(alarm, pi!!, schedule, alarmManager)
        } else {
            val recList = listOf(
                alarm.sunday,
                alarm.monday,
                alarm.tuesday,
                alarm.wednesday,
                alarm.thursday,
                alarm.friday,
                alarm.saturday,
            )
            Timber.d("Weekday: ${now[Calendar.DAY_OF_WEEK]}, ${Calendar.SUNDAY}")
            var alarmSet = false

            for (idx in now[Calendar.DAY_OF_WEEK] - 1..now[Calendar.DAY_OF_WEEK] + 5) {
                val idx2 = idx % 7 + 1
                Timber.d("werk: $idx2, ${recList[idx2 - 1]}")
                if (recList[idx2 - 1]) {
                    if (schedule > now) {
                        Timber.d("Next Alarm: $idx2, ${schedule[Calendar.DAY_OF_WEEK]}")
                        setAlarmManager(alarm, pi!!, schedule, alarmManager)
                        alarmSet = true
                        break
                    }
                }
                schedule.add(Calendar.DATE, 1)
            }
            if (!alarmSet) {
                setAlarmManager(alarm, pi!!, schedule, alarmManager)
            }
        }
    }

    private fun cancelAlarm(alarm: Alarm) {
        val alarmManager =
            requireContext().getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val pi = getPendingIntent(alarm)
        alarmManager.cancel(pi)
        Toast.makeText(
            context,
            "Alarm cancelled for ${alarm.time}",
            Toast.LENGTH_LONG
        ).show()
    }

    private fun getPendingIntent(alarm: Alarm): PendingIntent? {
        val i = Intent(context, AlarmReceiver::class.java)
        i.putExtra("ALARM_ID", alarm.id)
        return PendingIntent.getBroadcast(
            context,
            alarm.id!!.toInt(),
            i,
            PendingIntent.FLAG_UPDATE_CURRENT
        )
    }

    private fun setAlarmManager(
        alarm: Alarm,
        pi: PendingIntent,
        schedule: Calendar,
        alarmManager: AlarmManager
    ) {
        val info = AlarmManager.AlarmClockInfo(schedule.timeInMillis, pi)
        alarmManager.setAlarmClock(info, pi)
        Toast.makeText(
            context,
            "Alarm scheduled for ${alarm.time}",
            Toast.LENGTH_LONG
        ).show()
    }
}
