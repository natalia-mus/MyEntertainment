package com.example.myentertainment.view.main.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RatingBar
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.myentertainment.R
import com.example.myentertainment.data.Music
import com.example.myentertainment.interfaces.OnItemClickAction

class MusicAdapter(
    private val context: Context,
    private var music: List<Music>,
    private val onItemClickAction: OnItemClickAction
) :
    RecyclerView.Adapter<MusicViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MusicViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.single_music, parent, false)
        return MusicViewHolder(view)
    }

    override fun onBindViewHolder(holder: MusicViewHolder, position: Int) {
        holder.title.text = music[position].title
        music[position].rating?.let { it -> holder.rate.rating = it }

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


        holder.item.setOnClickListener() {
            val id = music[position].id
            onItemClickAction.onItemClicked(id)
        }

        holder.item.setOnLongClickListener() {
            val id = music[position].id
            onItemClickAction.onItemLongClicked(id)
            true
        }

    }

    override fun getItemCount() = music.size

    fun dataSetChanged(newDataSet: List<Music>) {
        music = newDataSet
        notifyDataSetChanged()
    }

}


class MusicViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val item = view.findViewById<ConstraintLayout>(R.id.music_item)

    val title = view.findViewById<TextView>(R.id.music_title)
    val artist = view.findViewById<TextView>(R.id.music_artist)
    val rate = view.findViewById<RatingBar>(R.id.music_rate)
    val genre = view.findViewById<TextView>(R.id.music_genre)
    val releaseYear = view.findViewById<TextView>(R.id.music_releaseYear)
}