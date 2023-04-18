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
import com.example.myentertainment.data.Movie
import com.example.myentertainment.interfaces.OnItemClickAction

class MoviesAdapter(
    private val context: Context,
    private var movies: List<Movie>,
    private val onItemClickAction: OnItemClickAction
) : RecyclerView.Adapter<MoviesViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MoviesViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.single_movie, parent, false)
        return MoviesViewHolder(view)
    }

    override fun onBindViewHolder(holder: MoviesViewHolder, position: Int) {
        holder.title.text = movies[position].title
        movies[position].rating?.let { it -> holder.rate.rating = it }

        if (movies[position].releaseYear.isNullOrEmpty()) {
            holder.releaseYear.visibility = View.GONE
        } else {
            holder.releaseYear.text = movies[position].releaseYear
        }

        if (movies[position].genre.isNullOrEmpty()) {
            holder.genre.text = context.getString(R.string.none)
        } else {
            holder.genre.text = movies[position].genre
        }

        if (movies[position].director.isNullOrEmpty()) {
            holder.director.text = context.getString(R.string.none)
        } else {
            holder.director.text = movies[position].director
        }


        holder.item.setOnClickListener() {
            val id = movies[position].id
            onItemClickAction.onItemClicked(id)
        }

        holder.item.setOnLongClickListener() {
            val id = movies[position].id
            onItemClickAction.onItemLongClicked(id)
            true
        }
    }

    override fun getItemCount() = movies.size

    fun dataSetChanged(newDataSet: List<Movie>) {
        movies = newDataSet
        notifyDataSetChanged()
    }

}

class MoviesViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val item: ConstraintLayout = view.findViewById(R.id.movie_item)

    val title: TextView = view.findViewById(R.id.movie_title)
    val releaseYear: TextView = view.findViewById(R.id.movie_releaseYear)
    val rate: RatingBar = view.findViewById(R.id.movie_rate)
    val genre: TextView = view.findViewById(R.id.movie_genre)
    val director: TextView = view.findViewById(R.id.movie_director)
}