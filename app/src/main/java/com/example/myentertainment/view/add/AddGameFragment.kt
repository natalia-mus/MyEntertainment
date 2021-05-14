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
import com.example.myentertainment.R
import com.example.myentertainment.`object`.CategoryObject
import com.example.myentertainment.data.Game
import com.example.myentertainment.interfaces.AddFragmentViewModelInterface
import com.example.myentertainment.viewmodel.add.AddGameFragmentViewModel

class AddGameFragment : Fragment(), AddFragmentViewModelInterface {

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

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        fragmentView = inflater.inflate(R.layout.fragment_add_game, container, false)
        initView()
        viewModel = ViewModelProvider(this).get(AddGameFragmentViewModel::class.java)
        setObservers()
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
        gameAddedMessage = getString(R.string.game_added)

        addButton.setOnClickListener() {
            val title = titleEditText.text.toString()
            val releaseYear = releaseYearEditText.text.toString()
            val genre = genreEditText.text.toString()
            val rate = ratingBar.rating

            val game = Game(title, releaseYear, genre, rate)
            viewModel.addToDatabase(game)
        }
    }

    override fun setObservers() {
        viewModel.loading.observe(this, { updateView(it, loadingSection) })
        viewModel.validationResult.observe(
            this,
            { validationResult(it, requireContext(), noTitleMessage) })
        viewModel.addingToDatabaseResult.observe(
            this,
            {
                addingToDatabaseResult(
                    it,
                    requireContext(),
                    gameAddedMessage,
                    CategoryObject.GAMES
                )
            })
    }
}