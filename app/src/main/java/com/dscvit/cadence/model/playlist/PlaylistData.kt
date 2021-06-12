package com.dscvit.cadence.model.playlist

data class PlaylistData(
    val href: String,
    val items: List<Item>,
    val limit: Int,
    val next: Any,
    val offset: Int,
    val previous: String,
    val total: Int
)