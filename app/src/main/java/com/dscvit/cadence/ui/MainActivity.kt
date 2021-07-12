package com.dscvit.cadence.ui

import android.content.Intent
import android.content.SharedPreferences
import android.graphics.PixelFormat
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.dscvit.cadence.R
import com.dscvit.cadence.alarm.NotificationHelper
import com.dscvit.cadence.ui.home.HomeViewModel
import com.dscvit.cadence.ui.login.LoginViewModel
import com.dscvit.cadence.util.SpotifyConstants
import com.spotify.sdk.android.auth.AuthorizationClient
import com.spotify.sdk.android.auth.AuthorizationRequest
import com.spotify.sdk.android.auth.AuthorizationResponse
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private val viewModel: LoginViewModel by viewModels()
    private val viewModel2: HomeViewModel by viewModels()
    private lateinit var prefs: SharedPreferences

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        window.setFormat(PixelFormat.RGBA_8888)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        prefs = getSharedPreferences("user_data", MODE_PRIVATE)
        if (!prefs.getBoolean("notif_channel", false)) {
            NotificationHelper.createNotificationChannel(
                this,
                "Alarms",
                "Channel to manage all alarms"
            )
            prefs.edit().putBoolean("notif_channel", true).apply()
        }
        viewModel.isLoggedIn(prefs.getBoolean("logged_in", false))
        viewModel.isConsented(prefs.getBoolean("consent", false))
        viewModel.isSuccessful(false)
        viewModel2.isSuccessful(false)
        viewModel.isSyncing(false)
        viewModel2.isSyncing(false)
        viewModel.isLoggedIn.observe(this, { loggedIn ->
            if (loggedIn) {
                if (viewModel.isSyncing.value == true) {
                    spotifySignIn()
                } else {
                    viewModel.isSuccessful(true)
                }
            }
        })

        viewModel2.isSyncing.observe(this, { sync ->
            if (sync) {
                spotifySignIn()
            }
        })

        viewModel.tokenData.observe(this, { tokenData ->
            if (tokenData.refresh_token != "" && tokenData.access_token != "" && tokenData != null) {
                prefs.edit().apply {
                    putString("token", tokenData.access_token)
                    putBoolean("logged_in", true)
                    putString("refresh_token", tokenData.refresh_token)
                    apply()
                }
                viewModel.isSuccessful(true)
                viewModel2.isSuccessful(true)
                viewModel.isSyncing(false)
                viewModel2.isSyncing(false)
            } else {
                Toast.makeText(this, "Unable to fetch token :(", Toast.LENGTH_LONG).show()
            }
        })
    }

    private fun spotifySignIn() {
        val builder =
            AuthorizationRequest.Builder(
                SpotifyConstants.CLIENT_ID,
                AuthorizationResponse.Type.CODE,
                SpotifyConstants.REDIRECT_URI
            )
        builder.setScopes(
            arrayOf(
                "app-remote-control",
                "user-read-email",
                "user-library-read",
                "playlist-read-private",
                "playlist-read-collaborative",
                "streaming"
            )
        )
        val request = builder.build()
        AuthorizationClient.openLoginActivity(
            this,
            SpotifyConstants.REQUEST_CODE,
            request
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, intent: Intent?) {
        super.onActivityResult(requestCode, resultCode, intent)
        if (requestCode == SpotifyConstants.REQUEST_CODE) {
            val response = AuthorizationClient.getResponse(resultCode, intent)
            when (response.type) {
                AuthorizationResponse.Type.TOKEN -> {
                    val token = response.accessToken
                    prefs.edit().apply {
                        putString("token", token)
                        putBoolean("logged_in", true)
                        apply()
                    }
                    viewModel.isSuccessful(true)
                    viewModel2.isSuccessful(true)
                    viewModel.isSyncing(false)
                    viewModel2.isSyncing(false)
                }
                AuthorizationResponse.Type.CODE -> {
                    val code = response.code
                    prefs.edit().apply {
                        putString("code", code)
                        apply()
                    }
                    viewModel.setCode(code)
                }
                AuthorizationResponse.Type.ERROR -> {
                    Toast.makeText(this, "Login Failed :(", Toast.LENGTH_LONG).show()
                    viewModel.isSuccessful(false)
                    viewModel2.isSuccessful(false)
                    viewModel.isSyncing(false)
                    viewModel2.isSyncing(false)
                }
                else -> {
                    viewModel.isSuccessful(false)
                    viewModel2.isSuccessful(false)
                    viewModel.isSyncing(false)
                    viewModel2.isSyncing(false)
                }
            }
        }
    }
}