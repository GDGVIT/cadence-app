package com.dscvit.cadence.model

import com.dscvit.cadence.model.remote.APIService
import javax.inject.Inject

class Repository
@Inject constructor(
    private val apiService: APIService
) {
    suspend fun getSpotifyData(token: String) = apiService.getUserData(token)
}