package com.dscvit.cadence.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dscvit.cadence.model.token.TokenData
import com.dscvit.cadence.repository.SpotifyRepository
import com.dscvit.cadence.utils.SpotifyConstants.CLIENT_DETAILS_ENCODED
import com.dscvit.cadence.utils.SpotifyConstants.REDIRECT_URI
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val repository: SpotifyRepository
) : ViewModel() {
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

    private val _code = MutableLiveData<String>()
    val code: LiveData<String> get() = _code

    fun setCode(code: String) {
        _code.value = code
        getTokenData()
    }

    private val _token = MutableLiveData<String>()
    val token: LiveData<String> get() = _token

    fun setToken(token: String) {
        _token.value = token
    }

    private val _tokenData = MutableLiveData<TokenData>()
    val tokenData: LiveData<TokenData>
        get() = _tokenData


    private fun getTokenData() = viewModelScope.launch {
        if (code.value != null) {
            Timber.d("STARTING CODE... ${code.value}")
            repository.getTokenData(
                "Basic $CLIENT_DETAILS_ENCODED",
                "authorization_code",
                "${code.value}",
                "$REDIRECT_URI"
            ).let { response ->
                if (response.isSuccessful) {
                    _tokenData.postValue(response.body())
                    response.body()?.let { setToken(it.access_token) }
                } else {
                    Timber.d("FAILED TO FETCH CODE ${response.raw()} ${code.value}")
                }
            }
        } else {
            Timber.d("NULL Failed CODE")
        }
    }

}
