package com.dscvit.cadence.ui.alarm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dscvit.cadence.repository.AlarmRepository
import dagger.hilt.android.lifecycle.HiltViewModel
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
    private val hour: LiveData<Int> get() = _hour
    private fun setHour(r: Int) {
        _hour.value = r
    }

    private val _min = MutableLiveData<Int>()
    private val min: LiveData<Int> get() = _min
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
            SimpleDateFormat(if (is24hr) "hh:mm" else "h:mm a", Locale.getDefault())
        val simpleDateFormat = SimpleDateFormat("hh:mm", Locale.getDefault())
        val timeDate: Date = simpleDateFormat.parse(t)!!
        _time.value = formatTime.format(timeDate)
    }
}
