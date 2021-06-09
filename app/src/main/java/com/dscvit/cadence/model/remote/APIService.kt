package com.dscvit.cadence.model.remote

import com.dscvit.cadence.model.remote.data.JsonData
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header

interface APIService {

    @GET("v1/me/tracks?market=IN")
    suspend fun getUserData(@Header("Authorization") myToken: String?): Response<JsonData>

}