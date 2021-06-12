package com.dscvit.cadence.ui.tracks

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dscvit.cadence.model.playlist.PlaylistData
import com.dscvit.cadence.model.user.UserData
import com.dscvit.cadence.repository.SpotifyRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class TracksListViewModel
@Inject constructor(
    private val repository: SpotifyRepository
) : ViewModel() {
    private val _token = MutableLiveData<String>()
    private val token: LiveData<String> get() = _token

    fun setToken(t: String) {
        _token.value = t
        spotifyRequest()
        playlistRequest()
    }

    private val _resp = MutableLiveData<UserData>()
    val spotifyResp: LiveData<UserData>
        get() = _resp


    private fun spotifyRequest() = viewModelScope.launch {
        if (token.value != null) {
            Timber.d("STARTING... ${token.value}")
            repository.getUserData("Bearer ${token.value}").let { response ->
                if (response.isSuccessful) {
                    _resp.postValue(response.body())
                } else {
                    Timber.d("FAILED TO FETCH ${token.value}")
                }
            }
        } else {
            Timber.d("NULL Failed")
        }
    }

    private val _respPlay = MutableLiveData<PlaylistData>()
    val spotifyRespPlay: LiveData<PlaylistData>
        get() = _respPlay


    private fun playlistRequest() = viewModelScope.launch {
        if (token.value != null) {
            Timber.d("STARTING2... ${token.value}")
            repository.getPlaylistData("Bearer ${token.value}").let { response ->
                if (response.isSuccessful) {
                    _respPlay.postValue(response.body())
                } else {
                    Timber.d("FAILED TO FETCH2 ${token.value}")
                }
            }
        } else {
            Timber.d("NULL Failed2")
        }
    }
}