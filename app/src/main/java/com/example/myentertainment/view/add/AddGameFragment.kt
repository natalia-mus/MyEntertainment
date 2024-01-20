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

    private var itemId: String? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        fragmentView = inflater.inflate(R.layout.fragment_add_game, container, false)
        viewModel = ViewModelProvider(this).get(AddGameFragmentViewModel::class.java)
        setObservers()
        establishOpeningContext()
        initView()
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

        if (openingContext == OpeningContext.ADD) {
            prepareViewForAddContext()
        }
    }

    override fun setObservers() {
        viewModel.loading.observe(this) { updateView(it, loadingSection) }
        viewModel.game.observe(this) { prepareViewForEditContext(it) }
        viewModel.validationResult.observe(this) { handleValidationResult(it, requireContext(), noTitleMessage) }
        viewModel.addingToDatabaseResult.observe(this) { handleAddingToDatabaseResult(it, requireContext(), gameAddedMessage, CategoryObject.GAMES) }
    }

    private fun establishOpeningContext() {
        itemId = arguments?.getString(Constants.ID)

        if (itemId != null) {
            openingContext = OpeningContext.EDIT
            viewModel.getGame(itemId!!)

        } else openingContext = OpeningContext.ADD
    }

    private fun prepareViewForAddContext() {
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

    private fun prepareViewForEditContext(item: Game) {
        gameAddedMessage = getString(R.string.game_edited)
        addButton.text = getString(R.string.game_edit)

        addButton.setOnClickListener {
            val id = itemId
            val title = titleEditText.text.toString()
            val releaseYear = releaseYearEditText.text.toString()
            val genre = genreEditText.text.toString()
            val rating = ratingBar.rating

            val game = Game(id, title, releaseYear, genre, rating, item.creationDate)
            viewModel.updateItem(game)
        }

        titleEditText.setText(item.title)
        releaseYearEditText.setText(item.releaseYear)
        genreEditText.setText(item.genre)
        if (item.rating != null) ratingBar.rating = item.rating
    }

}