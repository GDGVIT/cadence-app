package com.dscvit.cadence.util

import com.dscvit.cadence.model.playlist.Item

interface PlaylistListener {
    fun onSelect(playlist: Item, position: Int)
}
