package com.example.myentertainment.view.add

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.RatingBar
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.myentertainment.Constants
import com.example.myentertainment.OpeningContext
import com.example.myentertainment.R
import com.example.myentertainment.`object`.CategoryObject
import com.example.myentertainment.data.Movie
import com.example.myentertainment.interfaces.AddFragmentViewModelInterface
import com.example.myentertainment.viewmodel.add.AddMovieFragmentViewModel

class AddMovieFragment : Fragment(), AddFragmentViewModelInterface {

    private lateinit var openingContext: OpeningContext
    private lateinit var fragmentView: View
    private lateinit var viewModel: AddMovieFragmentViewModel

    private lateinit var titleEditText: EditText
    private lateinit var releaseYearEditText: EditText
    private lateinit var genreEditText: EditText
    private lateinit var directorEditText: EditText
    private lateinit var ratingBar: RatingBar
    private lateinit var addButton: Button
    private lateinit var loadingSection: ConstraintLayout

    private lateinit var noTitleMessage: String
    private lateinit var movieAddedMessage: String

    private var itemId: String? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        fragmentView = inflater.inflate(R.layout.fragment_add_movie, container, false)
        viewModel = ViewModelProvider(this).get(AddMovieFragmentViewModel::class.java)
        setObservers()
        establishOpeningContext()
        initView()
        return fragmentView
    }

    override fun initView() {
        titleEditText = fragmentView.findViewById(R.id.addMovie_title)
        releaseYearEditText = fragmentView.findViewById(R.id.addMovie_releaseYear)
        genreEditText = fragmentView.findViewById(R.id.addMovie_genre)
        directorEditText = fragmentView.findViewById(R.id.addMovie_director)
        ratingBar = fragmentView.findViewById(R.id.addMovie_rating)
        addButton = fragmentView.findViewById(R.id.addMovie_addButton)
        loadingSection = fragmentView.findViewById(R.id.addMovie_loadingSection)
        noTitleMessage = getString(R.string.movie_no_title)

        if (openingContext == OpeningContext.ADD) {
            prepareViewForAddContext()
        }
    }

    override fun setObservers() {
        viewModel.loading.observe(this) { updateView(it, loadingSection) }
        viewModel.movie.observe(this) { prepareViewForEditContext(it) }
        viewModel.validationResult.observe(this) { handleValidationResult(it, requireContext(), noTitleMessage) }
        viewModel.addingToDatabaseResult.observe(this) { handleAddingToDatabaseResult(it, requireContext(), movieAddedMessage, CategoryObject.MOVIES) }
    }

    private fun establishOpeningContext() {
        itemId = arguments?.getString(Constants.ID)

        if (itemId != null) {
            openingContext = OpeningContext.EDIT
            viewModel.getMovie(itemId!!)

        } else openingContext = OpeningContext.ADD
    }

    private fun prepareViewForAddContext() {
        movieAddedMessage = getString(R.string.movie_added)

        addButton.setOnClickListener() {
            val title = titleEditText.text.toString()
            val releaseYear = releaseYearEditText.text.toString()
            val genre = genreEditText.text.toString()
            val director = directorEditText.text.toString()
            val rating = ratingBar.rating

            val movie = Movie(null, title, releaseYear, genre, director, rating)
            viewModel.addToDatabase(movie)
        }
    }

    private fun prepareViewForEditContext(item: Movie) {
        movieAddedMessage = getString(R.string.movie_edited)
        addButton.text = getString(R.string.movie_edit)

        addButton.setOnClickListener() {
            val id = itemId
            val title = titleEditText.text.toString()
            val releaseYear = releaseYearEditText.text.toString()
            val genre = genreEditText.text.toString()
            val director = directorEditText.text.toString()
            val rating = ratingBar.rating

            val movie = Movie(id, title, releaseYear, genre, director, rating)
            viewModel.updateItem(movie)
        }

        titleEditText.setText(item.title)
        releaseYearEditText.setText(item.releaseYear)
        genreEditText.setText(item.genre)
        directorEditText.setText(item.director)
        if (item.rating != null) ratingBar.rating = item.rating
    }

}