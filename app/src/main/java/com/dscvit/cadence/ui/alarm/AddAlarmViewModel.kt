package com.dscvit.cadence.ui.alarm

import androidx.lifecycle.ViewModel
import com.dscvit.cadence.repository.SpotifyRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AddAlarmViewModel
@Inject constructor(
    private val repository: SpotifyRepository
) : ViewModel() {

}
