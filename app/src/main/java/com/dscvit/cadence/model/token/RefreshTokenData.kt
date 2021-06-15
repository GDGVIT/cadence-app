package com.dscvit.cadence.model.token

data class RefreshTokenData(
    val access_token: String,
    val expires_in: Int,
    val scope: String,
    val token_type: String
)