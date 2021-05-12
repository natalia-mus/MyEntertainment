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
import com.example.myentertainment.data.Music
import com.example.myentertainment.view.main.MainActivity
import com.example.myentertainment.viewmodel.AddMusicFragmentViewModel

class AddMusicFragment : Fragment() {

    private lateinit var fragmentView: View
    private lateinit var viewModel: AddMusicFragmentViewModel

    private lateinit var titleEditText: EditText
    private lateinit var artistEditText: EditText
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
        fragmentView = inflater.inflate(R.layout.fragment_add_music, container, false)
        initView()
        viewModel = ViewModelProvider(this).get(AddMusicFragmentViewModel::class.java)
        setObservers()
        return fragmentView
    }

    private fun initView() {
        titleEditText = fragmentView.findViewById(R.id.addMusic_title)
        artistEditText = fragmentView.findViewById(R.id.addMusic_artist)
        releaseYearEditText = fragmentView.findViewById(R.id.addMusic_releaseYear)
        genreEditText = fragmentView.findViewById(R.id.addMusic_genre)
        ratingBar = fragmentView.findViewById(R.id.addMusic_rating)
        addButton = fragmentView.findViewById(R.id.addMusic_addButton)
        loadingSection = fragmentView.findViewById(R.id.addMusic_loadingSection)

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
                "Please, enter song title",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun addingToDatabaseResult(addingToDatabaseResult: Boolean) {
        if (addingToDatabaseResult) {
            Toast.makeText(activity!!.applicationContext, "Song added.", Toast.LENGTH_LONG)
                .show()
            val intent = Intent(activity, MainActivity::class.java)
            startActivity(intent)
        } else {
            Toast.makeText(activity, "An error occured.", Toast.LENGTH_LONG).show()
        }
    }
}