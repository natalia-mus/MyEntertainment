package com.example.myentertainment.view.main.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myentertainment.R
import com.example.myentertainment.data.Book

class BooksAdapter(private val context: Context, private val books: List<Book>) :
    RecyclerView.Adapter<BooksRecyclerViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BooksRecyclerViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.single_book, parent, false)
        return BooksRecyclerViewHolder(view)
    }

    override fun onBindViewHolder(holder: BooksRecyclerViewHolder, position: Int) {
        holder.title.text = books[position].title
        holder.rate.rating = books[position].rating!!

        if (books[position].author.isNullOrEmpty()) {
            holder.author.visibility = View.GONE
        } else {
            holder.author.text = books[position].author
        }

        if (books[position].genre.isNullOrEmpty()) {
            holder.genre.text = "-"
        } else {
            holder.genre.text = books[position].genre
        }

        if (books[position].releaseYear.isNullOrEmpty()) {
            holder.releaseYear.text = "-"
        } else {
            holder.releaseYear.text = books[position].releaseYear
        }
    }

    override fun getItemCount() = books.size
}

class BooksRecyclerViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val title = view.findViewById<TextView>(R.id.book_title)
    val author = view.findViewById<TextView>(R.id.book_author)
    val rate = view.findViewById<RatingBar>(R.id.book_rate)
    val genre = view.findViewById<TextView>(R.id.book_genre)
    val releaseYear = view.findViewById<TextView>(R.id.book_releaseYear)
}