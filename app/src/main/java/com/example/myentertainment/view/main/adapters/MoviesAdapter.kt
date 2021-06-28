package com.example.myentertainment.view.main.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myentertainment.R
import com.example.myentertainment.data.Movie

class MoviesAdapter(private val context: Context, private val movies: List<Movie>) :
    RecyclerView.Adapter<MoviesViewHolder>() {

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
            holder.genre.text = "-"
        } else {
            holder.genre.text = movies[position].genre
        }

        if (movies[position].director.isNullOrEmpty()) {
            holder.director.text = "-"
        } else {
            holder.director.text = movies[position].director
        }
    }

    override fun getItemCount() = movies.size
}

class MoviesViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val title = view.findViewById<TextView>(R.id.movie_title)
    val releaseYear = view.findViewById<TextView>(R.id.movie_releaseYear)
    val rate = view.findViewById<RatingBar>(R.id.movie_rate)
    val genre = view.findViewById<TextView>(R.id.movie_genre)
    val director = view.findViewById<TextView>(R.id.movie_director)
}