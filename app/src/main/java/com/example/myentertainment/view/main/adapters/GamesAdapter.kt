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
import com.example.myentertainment.data.Game
import com.example.myentertainment.data.IEntertainment
import com.example.myentertainment.interfaces.OnItemClickAction

class GamesAdapter(
    private val context: Context,
    private var games: List<Game>,
    private val onItemClickAction: OnItemClickAction
) : RecyclerView.Adapter<GamesViewHolder>(), IEntertainmentAdapter {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GamesViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.single_game, parent, false)
        return GamesViewHolder(view)
    }

    override fun onBindViewHolder(holder: GamesViewHolder, position: Int) {
        holder.title.text = games[position].title
        games[position].rating?.let { it -> holder.rate.rating = it }

        if (games[position].releaseYear.isNullOrEmpty()) {
            holder.releaseYear.visibility = View.GONE
        } else {
            holder.releaseYear.text = games[position].releaseYear
        }

        if (games[position].genre.isNullOrEmpty()) {
            holder.genre.text = context.getString(R.string.none)
        } else {
            holder.genre.text = games[position].genre
        }


        holder.item.setOnClickListener {
            onItemClickAction.onItemClicked(games[position])
        }

        holder.item.setOnLongClickListener() {
            val id = games[position].id
            onItemClickAction.onItemLongClicked(id)
            true
        }
    }

    override fun getItemCount() = games.size

    override fun dataSetChanged(newDataSet: List<IEntertainment>) {
        games = newDataSet as List<Game>
        notifyDataSetChanged()
    }

}

class GamesViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val item: ConstraintLayout = view.findViewById(R.id.game_item)

    val title: TextView = view.findViewById(R.id.game_title)
    val releaseYear: TextView = view.findViewById(R.id.game_releaseYear)
    val rate: RatingBar = view.findViewById(R.id.game_rate)
    val genre: TextView = view.findViewById(R.id.game_genre)
}