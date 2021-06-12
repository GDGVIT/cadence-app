package com.dscvit.cadence.ui.login

import android.content.Intent
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor() : ViewModel() {
    private val _isLoggedIn = MutableLiveData<Boolean>()
    val isLoggedIn: LiveData<Boolean> get() = _isLoggedIn

    fun isLoggedIn(login: Boolean) {
        _isLoggedIn.value = login
    }

    private val _isSuccessful = MutableLiveData<Boolean>()
    val isSuccessful: LiveData<Boolean> get() = _isSuccessful

    fun isSuccessful(success: Boolean) {
        _isSuccessful.value = success
    }

    fun onSpotifyResult(requestCode: Int, resultCode: Int, intent: Intent?) {
        // TODO("onSpotifyResult()")
    }
}
