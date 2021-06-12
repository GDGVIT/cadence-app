package com.dscvit.cadence.api

import com.dscvit.cadence.model.playlist.PlaylistData
import com.dscvit.cadence.model.user.UserData
import retrofit2.Response
import retrofit2.http.*

interface ApiService {
    @Headers(
        "Accept: application/json",
        "Content-Type: application/json"
    )
    @GET("me")
    suspend fun getUserData(@Header("Authorization") token: String?): Response<UserData>

    @Headers(
        "Accept: application/json",
        "Content-Type: application/json"
    )
    @GET("me/playlists")
    suspend fun getPlaylistData(
        @Header("Authorization") token: String?,
        @Query("limit") limit: Int = 50,
        @Query("offset") offset: Int = 0
    ): Response<PlaylistData>
}