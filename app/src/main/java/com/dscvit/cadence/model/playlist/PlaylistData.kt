package com.dscvit.cadence.model.playlist

data class PlaylistData(
    val href: String,
    var items: List<Item>,
    val limit: Int,
    val next: Any,
    val offset: Int,
    val previous: Any,
    val total: Int
)
