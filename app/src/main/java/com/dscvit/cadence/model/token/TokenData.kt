package com.dscvit.cadence.model.token

data class TokenData(
    val access_token: String,
    val expires_in: Int,
    val refresh_token: String,
    val scope: String,
    val token_type: String
)
