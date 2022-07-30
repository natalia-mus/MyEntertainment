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
import com.example.myentertainment.data.Music
import com.example.myentertainment.interfaces.AddFragmentViewModelInterface
import com.example.myentertainment.viewmodel.add.AddMusicFragmentViewModel

class AddMusicFragment : Fragment(), AddFragmentViewModelInterface {

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

    private var itemId: String? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        fragmentView = inflater.inflate(R.layout.fragment_add_music, container, false)
        viewModel = ViewModelProvider(this).get(AddMusicFragmentViewModel::class.java)
        setObservers()
        establishOpeningContext()
        initView()
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

        if (openingContext == OpeningContext.ADD) {
            prepareViewForAddContext()
        }
    }

    override fun setObservers() {
        viewModel.loading.observe(this, { updateView(it, loadingSection) })
        viewModel.song.observe(this, { prepareViewForEditContext(it) })
        viewModel.validationResult.observe(this, { validationResult(it, requireContext(), noTitleMessage) })
        viewModel.addingToDatabaseResult.observe(this, { addingToDatabaseResult(
            it,
            requireContext(),
            songAddedMessage,
            CategoryObject.MUSIC
        )
        })
    }

    private fun establishOpeningContext() {
        itemId = arguments?.getString(Constants.ID)

        if (itemId != null) {
            openingContext = OpeningContext.EDIT
            viewModel.getSong(itemId!!)

        } else openingContext = OpeningContext.ADD
    }

    private fun prepareViewForAddContext() {
        songAddedMessage = getString(R.string.music_added)

        addButton.setOnClickListener() {
            val title = titleEditText.text.toString()
            val artist = artistEditText.text.toString()
            val releaseYear = releaseYearEditText.text.toString()
            val genre = genreEditText.text.toString()
            val rating = ratingBar.rating

            val music = Music(null, title, artist, releaseYear, genre, rating)
            viewModel.addToDatabase(music)
        }
    }

    private fun prepareViewForEditContext(item: Music) {
        songAddedMessage = getString(R.string.music_edited)
        addButton.text = getString(R.string.music_edit)

        addButton.setOnClickListener() {
            val id = itemId
            val title = titleEditText.text.toString()
            val artist = artistEditText.text.toString()
            val releaseYear = releaseYearEditText.text.toString()
            val genre = genreEditText.text.toString()
            val rating = ratingBar.rating

            val music = Music(id, title, artist, releaseYear, genre, rating)
            viewModel.updateItem(music)
        }

        titleEditText.setText(item.title)
        artistEditText.setText(item.artist)
        releaseYearEditText.setText(item.releaseYear)
        genreEditText.setText(item.genre)
        if (item.rating != null) ratingBar.rating = item.rating
    }

}