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
import com.example.myentertainment.data.Music
import com.example.myentertainment.interfaces.IAddToDatabase
import com.example.myentertainment.viewmodel.add.AddMusicFragmentViewModel

class AddMusicFragment : Fragment(), IAddToDatabase {

    private lateinit var openingContext: OpeningContext
    private lateinit var fragmentView: View
    private lateinit var viewModel: AddMusicFragmentViewModel

    private lateinit var titleEditText: EditText
    private lateinit var artistEditText: EditText
    private lateinit var releaseYearEditText: EditText
    private lateinit var genreEditText: EditText
    private lateinit var ratingBar: RatingBar
    private lateinit var addButton: Button
    private lateinit var loadingSection: ConstraintLayout

    private lateinit var noTitleMessage: String
    private lateinit var songAddedMessage: String

    private var song: Music? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        fragmentView = inflater.inflate(R.layout.fragment_add_music, container, false)
        viewModel = ViewModelProvider(this).get(AddMusicFragmentViewModel::class.java)
        setObservers()
        initView()
        establishOpeningContext()
        return fragmentView
    }

    override fun initView() {
        titleEditText = fragmentView.findViewById(R.id.addMusic_title)
        artistEditText = fragmentView.findViewById(R.id.addMusic_artist)
        releaseYearEditText = fragmentView.findViewById(R.id.addMusic_releaseYear)
        genreEditText = fragmentView.findViewById(R.id.addMusic_genre)
        ratingBar = fragmentView.findViewById(R.id.addMusic_rating)
        addButton = fragmentView.findViewById(R.id.addMusic_addButton)
        loadingSection = fragmentView.findViewById(R.id.addMusic_loadingSection)
        noTitleMessage = getString(R.string.music_no_title)
    }

    override fun setObservers() {
        viewModel.loading.observe(this) { updateView(it, loadingSection) }
        viewModel.validationResult.observe(this) { handleValidationResult(it, requireContext(), noTitleMessage) }
        viewModel.addingToDatabaseResult.observe(this) { handleAddingToDatabaseResult(it, requireContext(), songAddedMessage, CategoryObject.MUSIC) }
    }

    private fun establishOpeningContext() {
        if (arguments?.containsKey(CategoryObject.MUSIC.categoryName) == true) {
            song = arguments!!.getParcelable(CategoryObject.MUSIC.categoryName)
            prepareViewForEditContext()

        } else {
            prepareViewForAddContext()
        }
    }

    private fun prepareViewForAddContext() {
        openingContext = OpeningContext.ADD
        songAddedMessage = getString(R.string.music_added)

        addButton.setOnClickListener {
            val title = titleEditText.text.toString()
            val artist = artistEditText.text.toString()
            val releaseYear = releaseYearEditText.text.toString()
            val genre = genreEditText.text.toString()
            val rating = ratingBar.rating

            val music = Music(null, title, artist, releaseYear, genre, rating)
            viewModel.addToDatabase(music)
        }
    }

    private fun prepareViewForEditContext() {
        if (song != null) {
            openingContext = OpeningContext.EDIT
            songAddedMessage = getString(R.string.music_edited)
            addButton.text = getString(R.string.music_edit)

            addButton.setOnClickListener {
                song!!.title = titleEditText.text.toString()
                song!!.artist = artistEditText.text.toString()
                song!!.releaseYear = releaseYearEditText.text.toString()
                song!!.genre = genreEditText.text.toString()
                song!!.rating = ratingBar.rating

                viewModel.updateItem(song!!)
            }

            titleEditText.setText(song!!.title)
            artistEditText.setText(song!!.artist)
            releaseYearEditText.setText(song!!.releaseYear)
            genreEditText.setText(song!!.genre)
            val rating = song!!.rating
            if (rating != null) {
                ratingBar.rating = rating
            }
        }
    }

}