package com.dscvit.cadence.adapter

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.transition.DrawableCrossFadeFactory
import com.dscvit.cadence.R
import com.dscvit.cadence.model.playlist.Item
import com.spotify.android.appremote.api.SpotifyAppRemote

class PlaylistAdapter(
    private var playlists: List<Item>,
    private val spotifyAppRemote: SpotifyAppRemote
) :
    RecyclerView.Adapter<PlaylistAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val playlistTitle: TextView = view.findViewById(R.id.playlistTitle)
        val playlistSubtitle: TextView = view.findViewById(R.id.playlistSubtitle)
        val numberTracks: TextView = view.findViewById(R.id.numberTracks)
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
            numberTracks.text = currPlaylist.tracks.total.toString()
            val factory = DrawableCrossFadeFactory.Builder().setCrossFadeEnabled(true).build()
            Glide.with(imageView.context)
                .load(currPlaylist.images[0].url)
                .transition(DrawableTransitionOptions.withCrossFade(factory))
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .centerCrop()
                .placeholder(R.drawable.profile_pic_placeholder)
                .into(imageView)
            itemView.setOnClickListener {
                spotifyAppRemote.playerApi.play("spotify:playlist:${currPlaylist.id}")
            }
            itemView.setOnLongClickListener {
                val i = Intent(Intent.ACTION_VIEW)
                i.data = Uri.parse(currPlaylist.external_urls.spotify)
                itemView.context.startActivity(i)
                true
            }
        }
    }

    fun dataSetChange(newPlaylists: List<Item>) {
        playlists = newPlaylists
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = playlists.size
}
