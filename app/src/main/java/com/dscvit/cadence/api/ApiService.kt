package com.dscvit.cadence.api

import com.dscvit.cadence.model.ml.Song
import com.dscvit.cadence.model.playlist.PlaylistData
import com.dscvit.cadence.model.song.TracksData
import com.dscvit.cadence.model.token.RefreshTokenData
import com.dscvit.cadence.model.token.TokenData
import com.dscvit.cadence.model.user.UserData
import com.google.gson.JsonObject
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiService {
    @Headers(
        "Accept: application/json",
        "Content-Type: application/json"
    )
    @GET("https://api.spotify.com/v1/me")
    suspend fun getUserData(@Header("Authorization") token: String?): Response<UserData>

    @Headers(
        "Accept: application/json",
        "Content-Type: application/json"
    )
    @GET("https://api.spotify.com/v1/tracks")
    suspend fun getTracksData(
        @Header("Authorization") token: String?,
        @Query("ids") songId: String?
    ): Response<TracksData>

    @Headers(
        "Accept: application/json",
        "Content-Type: application/json"
    )
    @GET("https://api.spotify.com/v1/me/playlists")
    suspend fun getPlaylistData(
        @Header("Authorization") token: String?,
        @Query("limit") limit: Int = 50,
        @Query("offset") offset: Int = 0
    ): Response<PlaylistData>

    @FormUrlEncoded
    @POST("https://accounts.spotify.com/api/token")
    suspend fun getTokenData(
        @Header("Authorization") client_details: String,
        @Field("grant_type") grant_type: String,
        @Field("code") code: String,
        @Field("redirect_uri") redirect_uri: String,
    ): Response<TokenData>

    @FormUrlEncoded
    @POST("https://accounts.spotify.com/api/token")
    suspend fun getRefreshTokenData(
        @Header("Authorization") client_details: String,
        @Field("grant_type") grant_type: String,
        @Field("refresh_token") code: String,
    ): Response<RefreshTokenData>

    @Headers(
        "Accept: application/json",
        "Content-Type: application/json"
    )
    @POST("/playlist")
    suspend fun getSongData(
        @Body body: JsonObject,
    ): Response<Song>
}
