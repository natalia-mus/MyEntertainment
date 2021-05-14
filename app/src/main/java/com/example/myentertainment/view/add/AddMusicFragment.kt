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
import com.example.myentertainment.data.Music
import com.example.myentertainment.interfaces.AddFragmentViewModelInterface
import com.example.myentertainment.viewmodel.add.AddMusicFragmentViewModel

class AddMusicFragment : Fragment(), AddFragmentViewModelInterface {

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

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        fragmentView = inflater.inflate(R.layout.fragment_add_music, container, false)
        initView()
        viewModel = ViewModelProvider(this).get(AddMusicFragmentViewModel::class.java)
        setObservers()
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
        songAddedMessage = getString(R.string.music_added)

        addButton.setOnClickListener() {
            val title = titleEditText.text.toString()
            val artist = artistEditText.text.toString()
            val releaseYear = releaseYearEditText.text.toString()
            val genre = genreEditText.text.toString()
            val rating = ratingBar.rating

            val music = Music(title, artist, releaseYear, genre, rating)
            viewModel.addToDatabase(music)
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
                    songAddedMessage,
                    CategoryObject.MUSIC
                )
            })
    }
}