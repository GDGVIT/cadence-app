package com.dscvit.cadence.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.dscvit.cadence.R
import com.dscvit.cadence.model.playlist.Item
import com.spotify.android.appremote.api.SpotifyAppRemote

class PlaylistAdapter(
    private val playlists: List<Item>,
    private val spotifyAppRemote: SpotifyAppRemote
) :
    RecyclerView.Adapter<PlaylistAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val playlistTitle: TextView = view.findViewById(R.id.playlistTitle)
        val playlistSubtitle: TextView = view.findViewById(R.id.playlistSubtitle)
        val imageView: ImageView = view.findViewById(R.id.imageView)
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.item_playlist, viewGroup, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val currPlaylist = playlists[position]

        viewHolder.apply {
            playlistTitle.text = currPlaylist.name
            playlistSubtitle.text = "by ${currPlaylist.owner.display_name}"
            imageView.load(currPlaylist.images[0].url) {
                crossfade(true)
                crossfade(1000)
            }
            itemView.setOnClickListener {
                spotifyAppRemote.playerApi.play("spotify:playlist:${currPlaylist.id}");
            }
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = playlists.size

}