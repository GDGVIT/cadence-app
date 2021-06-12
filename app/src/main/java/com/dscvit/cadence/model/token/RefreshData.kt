package com.dscvit.cadence.model.token

data class RefreshData(
    val access_token: String,
    val expires_in: String,
    val refresh_token: String
)