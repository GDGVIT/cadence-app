package com.dscvit.cadence.repository

import com.dscvit.cadence.api.ApiService
import com.google.gson.JsonObject
import javax.inject.Inject

class SpotifyRepository
@Inject
constructor(private val apiService: ApiService) {
    suspend fun getUserData(token: String?) = apiService.getUserData(token)

    suspend fun getTokenData(
        client_details: String,
        grant_type: String,
        code: String,
        redirect_uri: String,
    ) = apiService.getTokenData(client_details, grant_type, code, redirect_uri)

    suspend fun getRefreshTokenData(
        client_details: String,
        grant_type: String,
        refresh_token: String,
    ) = apiService.getRefreshTokenData(client_details, grant_type, refresh_token)

    suspend fun getPlaylistData(token: String?) = apiService.getPlaylistData(token)

    suspend fun getTracksData(token: String?, songId: String?) =
        apiService.getTracksData(token, songId)

    suspend fun getSongData(json: JsonObject) = apiService.getSongData(json)
}