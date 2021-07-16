package com.dscvit.cadence.ui.alarm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dscvit.cadence.model.alarm.Alarm
import com.dscvit.cadence.model.ml.Song
import com.dscvit.cadence.model.song.TracksData
import com.dscvit.cadence.repository.AlarmRepository
import com.dscvit.cadence.repository.SpotifyRepository
import com.google.gson.JsonObject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class AddAlarmViewModel
@Inject constructor(
    private val repository: AlarmRepository,
    private val repositoryApi: SpotifyRepository
) : ViewModel() {
    private val _is24hr = MutableLiveData<Boolean>()
    val is24hr: LiveData<Boolean> get() = _is24hr
    fun setIs24Hr(r: Boolean) {
        _is24hr.value = r
        if (time.value != null) {
            setTime(hour.value!!, min.value!!, r)
        } else {
            setTime(0, 0, r)
        }
    }

    private val _hour = MutableLiveData<Int>()
    val hour: LiveData<Int> get() = _hour
    private fun setHour(r: Int) {
        _hour.value = r
    }

    private val _min = MutableLiveData<Int>()
    val min: LiveData<Int> get() = _min
    private fun setMinute(r: Int) {
        _min.value = r
    }

    private val _time = MutableLiveData<String>()
    val time: LiveData<String> get() = _time
    fun setTime(hr: Int, min: Int, is24hr: Boolean) {
        setHour(hr)
        setMinute(min)
        val t = "$hr:$min"
        val formatTime =
            SimpleDateFormat(if (is24hr) "HH:mm" else "h:mm a", Locale.getDefault())
        val simpleDateFormat = SimpleDateFormat("hh:mm", Locale.getDefault())
        val timeDate: Date = simpleDateFormat.parse(t)!!
        _time.value = formatTime.format(timeDate)
    }

    private val _selectedPlaylist = MutableLiveData<Int>()
    val selectedPlaylist: LiveData<Int> get() = _selectedPlaylist
    fun setSelectedPlaylist(r: Int) {
        _selectedPlaylist.value = r
    }

    private val _playlistId = MutableLiveData<String>()
    private val playlistId: LiveData<String> get() = _playlistId
    fun setPlaylistId(r: String) {
        _playlistId.value = r
    }

    private val _songId = MutableLiveData<Song>()
    val songId: LiveData<Song> get() = _songId

    fun getSongData(name: String, days: List<Boolean>, token: String) = viewModelScope.launch {
        try {
            val postParam = JsonObject()
            postParam.addProperty("prompt", name)
            postParam.addProperty("playlist", playlistId.value)
            repositoryApi.getSongData(postParam).let { response ->
                if (response.isSuccessful) {
                    _songId.postValue(response.body())
                    response.body()?.let { getTracksData(name, days, it, token) }
                } else {
                    setAlarmInserted(-1)
                    Timber.d("Failed to fetch song")
                }
            }
        } catch (e: Exception) {
            Timber.d("Failed to fetch song: $e")
            setAlarmInserted(-3)
        }
    }

    private val _alarmId = MutableLiveData<Long>()
    val alarmId: LiveData<Long> get() = _alarmId
    fun setAlarmId(r: Long) {
        _alarmId.postValue(r)
    }

    private val _alarmInserted = MutableLiveData<Int>()
    val alarmInserted: LiveData<Int> get() = _alarmInserted
    fun setAlarmInserted(r: Int) {
        _alarmInserted.postValue(r)
    }

    private fun insertAlarm(
        name: String,
        days: List<Boolean>,
        sid: Song,
        td: TracksData
    ): Long {
        if (sid.intent == null || sid.intent == "") sid.intent = "nan"
        if (sid.song != null && sid.song != "") {

            val alarm = Alarm(
                alarmName = name,
                hour = hour.value!!,
                minute = min.value!!,
                monday = days[0],
                tuesday = days[1],
                wednesday = days[2],
                thursday = days[3],
                friday = days[4],
                saturday = days[5],
                sunday = days[6],
                isRepeating = days.contains(true),
                isOn = true,
                playlistId = playlistId.value!!,
                songId = sid.song,
                type = sid.intent,
                songUrl = td.tracks[0].href,
                songArt = td.tracks[0].album.images[0].url,
                songName = td.tracks[0].name,
                songArtist = td.tracks[0].artists[0].name,
            )
            var id: Long = 0
            viewModelScope.launch(Dispatchers.IO) {
                id = repository.insertAlarm(alarm)
                setAlarmId(id)
                setAlarmInserted(3)
            }
            return id
        }
        Timber.d("WOW: ${playlistId.value} ${sid.song}")
        setAlarmInserted(-1)
        return -1
    }

    private val _trackData = MutableLiveData<TracksData>()
    val trackData: LiveData<TracksData> get() = _trackData

    private fun getTracksData(name: String, days: List<Boolean>, sid: Song, token: String) =
        viewModelScope.launch {
            try {
                repositoryApi.getTracksData(
                    "Bearer $token",
                    sid.song.substring(14, sid.song.length)
                )
                    .let { response ->
                        if (response.isSuccessful) {
                            setAlarmInserted(2)
                            _trackData.postValue(response.body())
                            response.body()?.let { insertAlarm(name, days, sid, it) }
                        } else {
                            setAlarmInserted(-2)
                            Timber.d("Failed to fetch song data $token ${response.raw()}")
                        }
                    }
            } catch (e: Exception) {
                setAlarmInserted(-2)
            }
        }

    init {
        setSelectedPlaylist(0)
    }
}
