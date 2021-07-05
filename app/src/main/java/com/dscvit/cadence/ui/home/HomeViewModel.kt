package com.dscvit.cadence.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dscvit.cadence.model.playlist.PlaylistData
import com.dscvit.cadence.model.token.RefreshTokenData
import com.dscvit.cadence.model.user.UserData
import com.dscvit.cadence.repository.SpotifyRepository
import com.dscvit.cadence.utils.SpotifyConstants
import com.spotify.android.appremote.api.SpotifyAppRemote
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class HomeViewModel
@Inject constructor(
    private val repository: SpotifyRepository
) : ViewModel() {
    private val _refreshToken = MutableLiveData<String>()
    private val refreshToken: LiveData<String> get() = _refreshToken

    fun setRefreshToken(r: String) {
        if (r != _refreshToken.value) {
            _refreshToken.value = r
            getTokenData()
        }
    }

    private val _tokenData = MutableLiveData<RefreshTokenData>()
    val tokenData: LiveData<RefreshTokenData>
        get() = _tokenData

    private fun getTokenData() = viewModelScope.launch {
        if (refreshToken.value != null) {
            Timber.d("STARTING REF... ${refreshToken.value}")
            repository.getRefreshTokenData(
                "Basic ${SpotifyConstants.CLIENT_DETAILS_ENCODED}",
                "refresh_token",
                "${refreshToken.value}"
            ).let { response ->
                if (response.isSuccessful) {
                    _tokenData.postValue(response.body())
                    response.body()?.let { setToken(it.access_token) }
                } else {
                    Timber.d("FAILED TO FETCH REF ${response.raw()} ${refreshToken.value}")
                }
            }
        } else {
            Timber.d("NULL Failed REF")
        }
    }

    private val _token = MutableLiveData<String>()
    val token: LiveData<String> get() = _token
    private fun setToken(t: String) {
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

    private val _isSyncing = MutableLiveData<Boolean>()
    val isSyncing: LiveData<Boolean> get() = _isSyncing
    fun isSyncing(sync: Boolean) {
        _isSyncing.value = sync
    }

    private val _isSuccessful = MutableLiveData<Boolean>()
    val isSuccessful: LiveData<Boolean> get() = _isSuccessful
    fun isSuccessful(success: Boolean) {
        _isSuccessful.value = success
    }

    private val _spotifyAppRemote = MutableLiveData<SpotifyAppRemote>()
    val spotifyAppRemote: LiveData<SpotifyAppRemote> get() = _spotifyAppRemote
    fun spotifyAppRemote(success: SpotifyAppRemote) {
        _spotifyAppRemote.value = success
    }

    private val _page = MutableLiveData<Int>()
    val page: LiveData<Int> get() = _page
    fun setPage(r: Int) {
        _page.value = r
    }

    init {
        setPage(0)
    }

}