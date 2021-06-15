package com.dscvit.cadence.ui.home

import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.graphics.LinearGradient
import android.graphics.Shader
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade
import com.bumptech.glide.request.transition.DrawableCrossFadeFactory
import com.dscvit.cadence.R
import com.dscvit.cadence.adapter.PlaylistAdapter
import com.dscvit.cadence.databinding.FragmentHomeBinding
import com.dscvit.cadence.utils.SpotifyConstants.CLIENT_ID
import com.dscvit.cadence.utils.SpotifyConstants.REDIRECT_URI
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
    var firstTime = true


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.alarmView.apply {
            val paint = digitalClock.paint
            val width = paint.measureText(digitalClock.text.toString())
            val textShader: Shader = LinearGradient(
                0f, 0f, width, digitalClock.textSize, intArrayOf(
                    requireContext().getColor(R.color.orange_light),
                    requireContext().getColor(R.color.pink_light),
                ), null, Shader.TileMode.CLAMP
            )

            digitalClock.paint.shader = textShader
        }

        binding.syncPlaylist.hide()

        binding.bottomNav.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.playlists -> {
                    binding.apply {
                        titleBar.text = context?.getString(R.string.playlists) ?: "Playlists"
                        alarmView.contentAlarms.visibility = View.GONE
                        playlists.visibility = View.VISIBLE
                        addAlarm.hide()
                        syncPlaylist.show()
                    }
                    true
                }
                else -> {
                    binding.apply {
                        titleBar.text = context?.getString(R.string.alarms) ?: "Alarms"
                        playlists.visibility = View.GONE
                        alarmView.contentAlarms.visibility = View.VISIBLE
                        addAlarm.show()
                        syncPlaylist.hide()
                    }
                    true
                }
            }
        }

        prefs = requireContext().getSharedPreferences("user_data", MODE_PRIVATE)
//        token = prefs.getString("token", "").toString()
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


        binding.syncPlaylist.setOnClickListener {
            viewModel.isSyncing(true)
        }

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

                    imageUrl = result.images[0].url
                    Glide.with(requireContext())
                        .load(imageUrl)
                        .transition(withCrossFade(factory))
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .centerCrop()
                        .placeholder(R.drawable.profile_pic_placeholder)
                        .into(binding.profilePic)
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
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.spotifyAppRemote.value.let {
            SpotifyAppRemote.disconnect(it)
        }
    }
}