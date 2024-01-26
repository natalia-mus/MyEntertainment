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
import com.example.myentertainment.data.Game
import com.example.myentertainment.interfaces.IAddToDatabase
import com.example.myentertainment.viewmodel.add.AddGameFragmentViewModel

class AddGameFragment : Fragment(), IAddToDatabase {

    private lateinit var openingContext: OpeningContext
    private lateinit var fragmentView: View
    private lateinit var viewModel: AddGameFragmentViewModel

    private lateinit var titleEditText: EditText
    private lateinit var releaseYearEditText: EditText
    private lateinit var genreEditText: EditText
    private lateinit var ratingBar: RatingBar
    private lateinit var addButton: Button
    private lateinit var loadingSection: ConstraintLayout

    private lateinit var noTitleMessage: String
    private lateinit var gameAddedMessage: String

    private var game: Game? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        fragmentView = inflater.inflate(R.layout.fragment_add_game, container, false)
        viewModel = ViewModelProvider(this).get(AddGameFragmentViewModel::class.java)
        setObservers()
        initView()
        establishOpeningContext()
        return fragmentView
    }

    override fun initView() {
        titleEditText = fragmentView.findViewById(R.id.addGame_title)
        releaseYearEditText = fragmentView.findViewById(R.id.addGame_releaseYear)
        genreEditText = fragmentView.findViewById(R.id.addGame_genre)
        ratingBar = fragmentView.findViewById(R.id.addGame_rating)
        addButton = fragmentView.findViewById(R.id.addGame_addButton)
        loadingSection = fragmentView.findViewById(R.id.addGame_loadingSection)
        noTitleMessage = getString(R.string.game_no_title)
    }

    override fun setObservers() {
        viewModel.loading.observe(this) { updateView(it, loadingSection) }
        viewModel.validationResult.observe(this) { handleValidationResult(it, requireContext(), noTitleMessage) }
        viewModel.addingToDatabaseResult.observe(this) { handleAddingToDatabaseResult(it, requireContext(), gameAddedMessage, CategoryObject.GAMES) }
    }

    private fun establishOpeningContext() {
        if (arguments?.containsKey(CategoryObject.GAMES.categoryName) == true) {
            game = arguments!!.getParcelable(CategoryObject.GAMES.categoryName)
            prepareViewForEditContext()

        } else {
            prepareViewForAddContext()
        }
    }

    private fun prepareViewForAddContext() {
        openingContext = OpeningContext.ADD
        gameAddedMessage = getString(R.string.game_added)

        addButton.setOnClickListener {
            val title = titleEditText.text.toString()
            val releaseYear = releaseYearEditText.text.toString()
            val genre = genreEditText.text.toString()
            val rate = ratingBar.rating

            val game = Game(null, title, releaseYear, genre, rate)
            viewModel.addToDatabase(game)
        }
    }

    private fun prepareViewForEditContext() {
        if (game != null) {
            openingContext = OpeningContext.EDIT
            gameAddedMessage = getString(R.string.game_edited)
            addButton.text = getString(R.string.game_edit)

            addButton.setOnClickListener {
                game!!.title = titleEditText.text.toString()
                game!!.releaseYear = releaseYearEditText.text.toString()
                game!!.genre = genreEditText.text.toString()
                game!!.rating = ratingBar.rating

                viewModel.updateItem(game!!)
            }

            titleEditText.setText(game!!.title)
            releaseYearEditText.setText(game!!.releaseYear)
            genreEditText.setText(game!!.genre)
            val rating = game!!.rating
            if (rating != null) {
                ratingBar.rating = rating
            }
        }
    }

}