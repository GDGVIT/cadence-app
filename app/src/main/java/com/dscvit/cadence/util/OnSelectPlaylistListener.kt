package com.dscvit.cadence.util

import com.dscvit.cadence.model.playlist.Item

interface OnSelectPlaylistListener {
    fun onSelect(playlist: Item, position: Int)
}
