package com.dscvit.cadence.ui.home

import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
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
import com.dscvit.cadence.databinding.FragmentHomeBinding
import com.dscvit.cadence.model.alarm.Alarm
import com.dscvit.cadence.util.OnEditAlarmListener
import com.dscvit.cadence.util.SpotifyConstants.CLIENT_ID
import com.dscvit.cadence.util.SpotifyConstants.REDIRECT_URI
import com.spotify.android.appremote.api.ConnectionParams
import com.spotify.android.appremote.api.Connector
import com.spotify.android.appremote.api.SpotifyAppRemote
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber


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
        inflater: LayoutInflater, container: ViewGroup?,
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

        viewModel.page.observe(viewLifecycleOwner, { p ->
            when (p) {
                R.id.playlists -> {
                    binding.apply {
                        titleBar.text = context?.getString(R.string.playlists) ?: "Playlists"
                        alarms.visibility = View.GONE
                        playlists.visibility = View.VISIBLE
                        addAlarm.hide()
                        syncPlaylist.show()
                    }
                }
                else -> {
                    binding.apply {
                        titleBar.text = context?.getString(R.string.alarms) ?: "Alarms"
                        playlists.visibility = View.GONE
                        alarms.visibility = View.VISIBLE
                        addAlarm.show()
                        syncPlaylist.hide()
                    }
                }
            }
        })

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

        viewModel.alarmsList.observe(viewLifecycleOwner, { t ->
            if (t != null) {
                if (firstTimeAlarm) {
                    alarmAdapter = AlarmAdapter(t, object : OnEditAlarmListener {
                        override fun onToggle(alarm: Alarm) {
                            viewModel.updateAlarm(alarm)
                        }

                        override fun onDelete(alarm: Alarm) {
                            viewModel.deleteAlarm(alarm.id!!)
                        }
                    })
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
            }
        })

        viewModel.token.observe(viewLifecycleOwner, { t ->
            if (t != "" && t != null) {
                token = t
                prefs.edit().putString("token", token).apply()
            }
        })

        try {
            viewModel.isSuccessful.observe(
                viewLifecycleOwner, { successful ->
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
                        putString("imageUrl", result.images[0].url)
                        putString("email", result.email)
                        apply()
                    }

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
                })

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
                })

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
                })
        } catch (e: Exception) {
            Toast.makeText(context, "Something went wrong :(", Toast.LENGTH_SHORT).show()
        }

        binding.profilePic.setOnClickListener {
            Toast.makeText(context, "Wanna logout? Nub", Toast.LENGTH_LONG).show()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.spotifyAppRemote.value.let {
            SpotifyAppRemote.disconnect(it)
        }
    }
}