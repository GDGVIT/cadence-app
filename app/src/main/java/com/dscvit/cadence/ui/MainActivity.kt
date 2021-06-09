package com.dscvit.cadence.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import com.dscvit.cadence.R
import com.dscvit.cadence.ui.auth.LoginViewModel
import com.dscvit.cadence.utils.SpotifyConstants
import com.spotify.sdk.android.auth.AuthorizationClient
import com.spotify.sdk.android.auth.AuthorizationRequest
import com.spotify.sdk.android.auth.AuthorizationResponse
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private val viewModel: LoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        viewModel.isSuccessful(false)
        viewModel.isLoggedIn.observe(this, { loggedIn ->
            if (loggedIn) {
                spotifySignIn()
            }
        })
    }

    private fun spotifySignIn() {
        val builder =
            AuthorizationRequest.Builder(
                SpotifyConstants.CLIENT_ID,
                AuthorizationResponse.Type.TOKEN,
                SpotifyConstants.REDIRECT_URI
            )
        builder.setScopes(arrayOf("user-library-read"))
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
                    Toast.makeText(this, "Login Successful", Toast.LENGTH_LONG).show()
                    viewModel.isSuccessful(true)
                }
                AuthorizationResponse.Type.ERROR -> {
                    Toast.makeText(this, "Login Failed", Toast.LENGTH_LONG).show()
                    viewModel.isSuccessful(false)
                }
                else -> {
                }
            }
        }
    }
}