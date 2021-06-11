package com.dscvit.cadence.repository

import com.dscvit.cadence.api.ApiService
import javax.inject.Inject

class SpotifyRepository
@Inject
constructor(private val apiService: ApiService) {
    suspend fun getSpotifyData(token: String) = apiService.getSpotifyData(token)
}