package com.dscvit.cadence.ui.alarm

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.dscvit.cadence.adapter.PlaylistSelectorAdapter
import com.dscvit.cadence.databinding.FragmentPlaylistsBinding
import com.dscvit.cadence.model.playlist.Item
import com.dscvit.cadence.ui.home.HomeViewModel
import com.dscvit.cadence.util.OnSelectPlaylistListener
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PlaylistsFragment : Fragment() {

    private val viewModel by activityViewModels<AddAlarmViewModel>()
    private val viewModelPlaylists by activityViewModels<HomeViewModel>()
    private var _binding: FragmentPlaylistsBinding? = null
    private val binding get() = _binding!!
    private var firstTime = true
    private lateinit var playlistAdapter: PlaylistSelectorAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPlaylistsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.back.setOnClickListener {
            requireActivity().onBackPressed()
        }
        viewModelPlaylists.spotifyRespPlay.observe(
            viewLifecycleOwner,
            { result ->
                if (firstTime) {
                    playlistAdapter =
                        PlaylistSelectorAdapter(
                            result.items,
                            object : OnSelectPlaylistListener {
                                override fun onSelect(playlist: Item, position: Int) {
                                    viewModel.setSelectedPlaylist(position)
                                    viewModel.setPlaylistId(playlist.id)
                                    requireActivity().onBackPressed()
                                }
                            }
                        )
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
    }
}
