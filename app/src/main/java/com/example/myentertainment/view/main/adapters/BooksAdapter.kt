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
import com.example.myentertainment.data.Book
import com.example.myentertainment.interfaces.OnItemClickAction

class BooksAdapter(
    private val context: Context,
    private var books: List<Book>,
    private val onItemClickAction: OnItemClickAction
) :
    RecyclerView.Adapter<BooksViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BooksViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.single_book, parent, false)
        return BooksViewHolder(view)
    }

    override fun onBindViewHolder(holder: BooksViewHolder, position: Int) {
        holder.title.text = books[position].title
        books[position].rating?.let { it -> holder.rate.rating = it }

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


        holder.item.setOnLongClickListener() {
            val id = books[position].id
            onItemClickAction.onItemLongClicked(id)
            true
        }
    }

    override fun getItemCount() = books.size

    fun dataSetChanged(newDataSet: List<Book>) {
        books = newDataSet
        notifyDataSetChanged()
    }

}

class BooksViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val item = view.findViewById<ConstraintLayout>(R.id.book_item)

    val title = view.findViewById<TextView>(R.id.book_title)
    val author = view.findViewById<TextView>(R.id.book_author)
    val rate = view.findViewById<RatingBar>(R.id.book_rate)
    val genre = view.findViewById<TextView>(R.id.book_genre)
    val releaseYear = view.findViewById<TextView>(R.id.book_releaseYear)
}