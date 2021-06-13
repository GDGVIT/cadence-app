package com.dscvit.cadence.ui.tracks

import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.dscvit.cadence.adapter.PlaylistAdapter
import com.dscvit.cadence.databinding.FragmentPlaylistBinding
import com.dscvit.cadence.utils.SpotifyConstants.CLIENT_ID
import com.dscvit.cadence.utils.SpotifyConstants.REDIRECT_URI
import com.spotify.android.appremote.api.ConnectionParams
import com.spotify.android.appremote.api.Connector
import com.spotify.android.appremote.api.SpotifyAppRemote
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class PlaylistFragment : Fragment() {

    private val viewModel by activityViewModels<TracksListViewModel>()
    private var _binding: FragmentPlaylistBinding? = null
    private val binding get() = _binding!!
    private lateinit var prefs: SharedPreferences
    private lateinit var playlistAdapter: PlaylistAdapter
    private lateinit var spotifyAppRemote: SpotifyAppRemote
    lateinit var token: String

    override fun onStop() {
        super.onStop()
        spotifyAppRemote.let {
            SpotifyAppRemote.disconnect(it)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPlaylistBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        prefs = requireContext().getSharedPreferences("user_data", MODE_PRIVATE)
        token = prefs.getString("token", "").toString()
        viewModel.spotifyResp.observe(
            viewLifecycleOwner,
            { result ->
                binding.username.text = "${result.display_name}'s Playlists"
                prefs.edit().apply {
                    putString("id", result.id)
                    putString("name", result.display_name)
                    putString("imageUrl", result.images[0].url)
                    putString("email", result.email)
                    apply()
                }
            })
        val connectionParams = ConnectionParams.Builder(CLIENT_ID)
            .setRedirectUri(REDIRECT_URI)
            .showAuthView(true)
            .build()

        SpotifyAppRemote.connect(context, connectionParams, object : Connector.ConnectionListener {
            override fun onConnected(appRemote: SpotifyAppRemote) {
                spotifyAppRemote = appRemote
                Timber.d("Connected! Yay!")
                viewModel.setToken(token)
            }

            override fun onFailure(throwable: Throwable) {
                Timber.e(throwable)
                // Something went wrong when attempting to connect! Handle errors here
            }
        })



        viewModel.spotifyRespPlay.observe(
            viewLifecycleOwner,
            { result ->
                playlistAdapter = PlaylistAdapter(result.items, spotifyAppRemote)
                binding.playlists.apply {
                    layoutManager = StaggeredGridLayoutManager(
                        2, StaggeredGridLayoutManager.VERTICAL
                    )
                    setHasFixedSize(true)
                    adapter = playlistAdapter
                }
            })
    }
}