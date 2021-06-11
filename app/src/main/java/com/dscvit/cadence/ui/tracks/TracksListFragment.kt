package com.dscvit.cadence.ui.tracks

import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.dscvit.cadence.databinding.FragmentTracksListBinding
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class TracksListFragment : Fragment() {

    private val viewModel by activityViewModels<TracksListViewModel>()
    private var _binding: FragmentTracksListBinding? = null
    private val binding get() = _binding!!
    lateinit var prefs: SharedPreferences
    lateinit var token: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTracksListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        prefs = requireContext().getSharedPreferences("user_data", MODE_PRIVATE)
        token = prefs.getString("token", "").toString()
        binding.tracks.text = token
        viewModel.setToken(token)
        viewModel.spotifyResponse.observe(
            viewLifecycleOwner,
            { result ->
                Timber.d("LOL! $result")
            })
    }
}