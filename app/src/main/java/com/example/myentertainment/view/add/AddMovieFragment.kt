package com.example.myentertainment.view.add

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.RatingBar
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.myentertainment.R
import com.example.myentertainment.data.Movie
import com.example.myentertainment.viewmodel.AddMovieFragmentViewModel

class AddMovieFragment : Fragment() {

    private lateinit var fragmentView: View
    private lateinit var viewModel: AddMovieFragmentViewModel

    private lateinit var titleEditText: EditText
    private lateinit var releaseYearEditText: EditText
    private lateinit var genreEditText: EditText
    private lateinit var directorEditText: EditText
    private lateinit var ratingBar: RatingBar
    private lateinit var addButton: Button

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        fragmentView = inflater.inflate(R.layout.fragment_add_movie, container, false)
        initView()
        viewModel = ViewModelProvider(this).get(AddMovieFragmentViewModel::class.java)
        return fragmentView
    }

    private fun initView() {
        titleEditText = fragmentView.findViewById(R.id.addMovie_title)
        releaseYearEditText = fragmentView.findViewById(R.id.addMovie_releaseYear)
        genreEditText = fragmentView.findViewById(R.id.addMovie_genre)
        directorEditText = fragmentView.findViewById(R.id.addMovie_director)
        ratingBar = fragmentView.findViewById(R.id.addMovie_rating)
        addButton = fragmentView.findViewById(R.id.addMovie_addButton)

        addButton.setOnClickListener() {
            val title = titleEditText.text.toString()
            val releaseYear = releaseYearEditText.text.toString()
            val genre = genreEditText.text.toString()
            val director = directorEditText.text.toString()
            val rating = ratingBar.rating

            val movie = Movie(title, releaseYear, genre, director, rating)
            viewModel.addToDatabase(movie)
        }
    }
}