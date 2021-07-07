package com.dscvit.cadence.ui.alarm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dscvit.cadence.model.alarm.Alarm
import com.dscvit.cadence.repository.AlarmRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

@HiltViewModel
class AddAlarmViewModel
@Inject constructor(
    private val repository: AlarmRepository
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
        val t = "${hr}:${min}"
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
    val playlistId: LiveData<String> get() = _playlistId
    fun setPlaylistId(r: String) {
        _playlistId.value = r
    }

    fun insertAlarm(name: String, days: List<Boolean>, pid: String): Long {
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
            playlistId = playlistId.value!!,
            songId = "DEMO",
            isOn = true
        )
        var id: Long = 0
        viewModelScope.launch(Dispatchers.IO) {
            id = repository.insertAlarm(alarm)
        }
        return id
    }

    init {
        setSelectedPlaylist(0)
    }
}
