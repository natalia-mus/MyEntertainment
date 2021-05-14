package com.example.myentertainment.view.main.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myentertainment.R
import com.example.myentertainment.data.Music

class MusicAdapter(private val context: Context, private val music: List<Music>) :
    RecyclerView.Adapter<MusicViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MusicViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.single_music, parent, false)
        return MusicViewHolder(view)
    }

    override fun onBindViewHolder(holder: MusicViewHolder, position: Int) {
        holder.title.text = music[position].title
        holder.rate.rating = music[position].rating!!

        if (music[position].artist.isNullOrEmpty()) {
            holder.artist.visibility = View.GONE
        } else {
            holder.artist.text = music[position].artist
        }

        if (music[position].genre.isNullOrEmpty()) {
            holder.genre.text = "-"
        } else {
            holder.genre.text = music[position].genre
        }

        if (music[position].releaseYear.isNullOrEmpty()) {
            holder.releaseYear.text = "-"
        } else {
            holder.releaseYear.text = music[position].releaseYear
        }

    }

    override fun getItemCount() = music.size
}


class MusicViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val title = view.findViewById<TextView>(R.id.music_title)
    val artist = view.findViewById<TextView>(R.id.music_artist)
    val rate = view.findViewById<RatingBar>(R.id.music_rate)
    val genre = view.findViewById<TextView>(R.id.music_genre)
    val releaseYear = view.findViewById<TextView>(R.id.music_releaseYear)
}