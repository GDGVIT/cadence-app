package com.dscvit.cadence.model

import com.dscvit.cadence.model.remote.APIService
import javax.inject.Inject

class Repository @Inject constructor(
    private val api: APIService
) {
    suspend fun getSpotifyData(myToken: String) = api.getUserData(myToken)
}