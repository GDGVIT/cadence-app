package com.dscvit.cadence.repository

import com.dscvit.cadence.api.ApiService
import javax.inject.Inject

class SpotifyRepository
@Inject
constructor(private val apiService: ApiService) {
    suspend fun getUserData(token: String?) = apiService.getUserData(token)
    suspend fun getPlaylistData(token: String?) = apiService.getPlaylistData(token)

}