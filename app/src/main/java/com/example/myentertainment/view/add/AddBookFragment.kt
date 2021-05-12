package com.example.myentertainment.view.add

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.RatingBar
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.myentertainment.R
import com.example.myentertainment.`object`.ValidationObject
import com.example.myentertainment.data.Book
import com.example.myentertainment.view.main.MainActivity
import com.example.myentertainment.viewmodel.AddBookFragmentViewModel

class AddBookFragment : Fragment() {

    private lateinit var fragmentView: View
    private lateinit var viewModel: AddBookFragmentViewModel

    private lateinit var titleEditText: EditText
    private lateinit var authorEditText: EditText
    private lateinit var releaseYearEditText: EditText
    private lateinit var genreEditText: EditText
    private lateinit var ratingBar: RatingBar
    private lateinit var addButton: Button
    private lateinit var loadingSection: ConstraintLayout

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        fragmentView = inflater.inflate(R.layout.fragment_add_book, container, false)
        initView()
        viewModel = ViewModelProvider(this).get(AddBookFragmentViewModel::class.java)
        setObservers()
        return fragmentView
    }

    private fun initView() {
        titleEditText = fragmentView.findViewById(R.id.addBook_title)
        authorEditText = fragmentView.findViewById(R.id.addBook_author)
        releaseYearEditText = fragmentView.findViewById(R.id.addBook_releaseYear)
        genreEditText = fragmentView.findViewById(R.id.addBook_genre)
        ratingBar = fragmentView.findViewById(R.id.addBook_rating)
        addButton = fragmentView.findViewById(R.id.addBook_addButton)
        loadingSection = fragmentView.findViewById(R.id.addBook_loadingSection)

        addButton.setOnClickListener() {
            val title = titleEditText.text.toString()
            val author = authorEditText.text.toString()
            val releaseYear = releaseYearEditText.text.toString()
            val genre = genreEditText.text.toString()
            val rating = ratingBar.rating

            val book = Book(title, author, releaseYear, genre, rating)
            viewModel.addToDatabase(book)
        }
    }

    private fun setObservers() {
        viewModel.loading.observe(this, { updateView(it) })
        viewModel.validationResult.observe(this, { validationResult(it) })
        viewModel.addingToDatabaseResult.observe(this, { addingToDatabaseResult(it) })
    }

    private fun updateView(loading: Boolean) {
        if (loading) {
            loadingSection.visibility = View.VISIBLE
        } else {
            loadingSection.visibility = View.INVISIBLE
        }
    }

    private fun validationResult(validationResult: Int) {
        when (validationResult) {
            ValidationObject.EMPTY_VALUES -> Toast.makeText(
                activity,
                "Please, enter book title",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun addingToDatabaseResult(addingToDatabaseResult: Boolean) {
        if (addingToDatabaseResult) {
            Toast.makeText(activity!!.applicationContext, "Book added.", Toast.LENGTH_LONG)
                .show()
            val intent = Intent(activity, MainActivity::class.java)
            startActivity(intent)
        } else {
            Toast.makeText(activity, "An error occured.", Toast.LENGTH_LONG).show()
        }
    }
}