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
import com.example.myentertainment.OpeningContext
import com.example.myentertainment.R
import com.example.myentertainment.`object`.CategoryObject
import com.example.myentertainment.data.Book
import com.example.myentertainment.interfaces.IAddToDatabase
import com.example.myentertainment.viewmodel.add.AddBookFragmentViewModel

class AddBookFragment : Fragment(), IAddToDatabase {

    private lateinit var openingContext: OpeningContext
    private lateinit var fragmentView: View
    private lateinit var viewModel: AddBookFragmentViewModel

    private lateinit var titleEditText: EditText
    private lateinit var authorEditText: EditText
    private lateinit var releaseYearEditText: EditText
    private lateinit var genreEditText: EditText
    private lateinit var ratingBar: RatingBar
    private lateinit var addButton: Button
    private lateinit var loadingSection: ConstraintLayout

    private lateinit var noTitleMessage: String
    private lateinit var bookAddedMessage: String

    private var book: Book? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        fragmentView = inflater.inflate(R.layout.fragment_add_book, container, false)
        viewModel = ViewModelProvider(this).get(AddBookFragmentViewModel::class.java)
        setObservers()
        initView()
        establishOpeningContext()
        return fragmentView
    }

    override fun initView() {
        titleEditText = fragmentView.findViewById(R.id.addBook_title)
        authorEditText = fragmentView.findViewById(R.id.addBook_author)
        releaseYearEditText = fragmentView.findViewById(R.id.addBook_releaseYear)
        genreEditText = fragmentView.findViewById(R.id.addBook_genre)
        ratingBar = fragmentView.findViewById(R.id.addBook_rating)
        addButton = fragmentView.findViewById(R.id.addBook_addButton)
        loadingSection = fragmentView.findViewById(R.id.addBook_loadingSection)
        noTitleMessage = getString(R.string.book_no_title)
    }

    override fun setObservers() {
        viewModel.loading.observe(this) { updateView(it, loadingSection) }
        viewModel.validationResult.observe(this) { handleValidationResult(it, requireContext(), noTitleMessage) }
        viewModel.addingToDatabaseResult.observe(this) { handleAddingToDatabaseResult(it, requireContext(), bookAddedMessage, CategoryObject.BOOKS) }
    }

    private fun establishOpeningContext() {
        if (arguments?.containsKey(CategoryObject.BOOKS.categoryName) == true) {
            book = arguments!!.getParcelable(CategoryObject.BOOKS.categoryName)
            prepareViewForEditContext()

        } else {
            prepareViewForAddContext()
        }
    }

    private fun prepareViewForAddContext() {
        openingContext = OpeningContext.ADD
        bookAddedMessage = getString(R.string.book_added)

        addButton.setOnClickListener {
            val title = titleEditText.text.toString()
            val author = authorEditText.text.toString()
            val releaseYear = releaseYearEditText.text.toString()
            val genre = genreEditText.text.toString()
            val rating = ratingBar.rating

            val book = Book(null, title, author, releaseYear, genre, rating)
            viewModel.addToDatabase(book)
        }
    }

    private fun prepareViewForEditContext() {
        if (book != null) {
            openingContext = OpeningContext.EDIT
            bookAddedMessage = getString(R.string.book_edited)
            addButton.text = getString(R.string.book_edit)

            addButton.setOnClickListener {
                book!!.title = titleEditText.text.toString()
                book!!.author = authorEditText.text.toString()
                book!!.releaseYear = releaseYearEditText.text.toString()
                book!!.genre = genreEditText.text.toString()
                book!!.rating = ratingBar.rating

                viewModel.updateItem(book!!)
            }

            titleEditText.setText(book!!.title)
            authorEditText.setText(book!!.author)
            releaseYearEditText.setText(book!!.releaseYear)
            genreEditText.setText(book!!.genre)
            val rating = book!!.rating
            if (rating != null) {
                ratingBar.rating = rating
            }
        }
    }

}