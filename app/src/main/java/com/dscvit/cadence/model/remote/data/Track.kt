package com.dscvit.cadence.model.remote.data

data class Track(
    val album: Album,
    val artists: List<Artist>,
    val external_urls: ExternalUrlsXXX,
    val href: String,
    val name: String,
    val preview_url: String,
    val uri: String
)