package com.dscvit.cadence.model.remote.data

data class Album(
    val artists: List<Artist>,
    val images: List<Image>,
    val name: String,
    val uri: String
)