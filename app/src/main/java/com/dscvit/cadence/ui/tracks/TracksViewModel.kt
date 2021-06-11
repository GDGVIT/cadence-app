package com.dscvit.cadence.ui.tracks

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dscvit.cadence.model.Repository
import com.dscvit.cadence.model.remote.data.JsonData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TracksListViewModel
@Inject constructor(
    private val repository: Repository
) : ViewModel() {
    private val _token = MutableLiveData<String>()
    private val token: LiveData<String> get() = _token

    fun setToken(t: String) {
        _token.value = t
    }

    private val _response = MutableLiveData<ArrayList<JsonData>>()
    val spotifyResponse: LiveData<ArrayList<JsonData>>
        get() = _response

    init {
        spotifyRequest()
    }

    private fun spotifyRequest() = viewModelScope.launch {
//        repository.getSpotifyData(token.toString()).let { response ->
//            if (response.isSuccessful) {
//                val json = response.body()!!.items
//                for (items in json) {
//                    Timber.d(items.toString())
//                }
//            } else {
//                Timber.d("FAILED TO FETCH TRACKS")
//            }
//        }
    }
}