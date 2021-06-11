package com.dscvit.cadence.api

import com.dscvit.cadence.model.UserData
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header

interface ApiService {
    @GET("v1/me")
    suspend fun getSpotifyData(@Header("Authorization") myToken: String?): Response<UserData>
}