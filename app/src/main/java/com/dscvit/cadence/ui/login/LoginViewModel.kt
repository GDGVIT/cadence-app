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

    private val _isConsented = MutableLiveData<Boolean>()
    val isConsented: LiveData<Boolean> get() = _isConsented

    fun isConsented(consent: Boolean) {
        _isConsented.value = consent
    }

    private val _isSyncing = MutableLiveData<Boolean>()
    val isSyncing: LiveData<Boolean> get() = _isSyncing

    fun isSyncing(sync: Boolean) {
        _isSyncing.value = sync
    }
}
