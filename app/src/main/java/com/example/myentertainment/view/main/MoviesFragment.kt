package com.example.myentertainment.view.main

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myentertainment.Constants
import com.example.myentertainment.R
import com.example.myentertainment.`object`.CategoryObject
import com.example.myentertainment.data.Movie
import com.example.myentertainment.interfaces.OnItemClickAction
import com.example.myentertainment.view.add.AddActivity
import com.example.myentertainment.view.main.adapters.MoviesAdapter
import com.example.myentertainment.viewmodel.main.MoviesFragmentViewModel

class MoviesFragment : Fragment(), OnItemClickAction {

    private lateinit var viewModel: MoviesFragmentViewModel
    private lateinit var fragmentView: View

    private lateinit var moviesList: RecyclerView
    private lateinit var moviesAdapter: MoviesAdapter
    private lateinit var loadingSection: ConstraintLayout
    private lateinit var noMoviesLabel: TextView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        fragmentView = inflater.inflate(R.layout.fragment_movies, container, false)
        viewModel = ViewModelProvider(this).get(MoviesFragmentViewModel::class.java)
        initView()
        setObservers()
        viewModel.fetchMovies()
        return fragmentView
    }

    override fun onItemClicked(id: String?) {
        editMovie(id)
    }

    override fun onItemLongClicked(id: String?) {
        viewModel.deleteMovie(id)
    }

    private fun editMovie(id: String?) {
        val intent = Intent(activity, AddActivity::class.java)
        intent.putExtra(Constants.CATEGORY, CategoryObject.MOVIES)
        intent.putExtra(Constants.ID, id!!)
        startActivity(intent)
    }

    private fun initView() {
        moviesList = fragmentView.findViewById(R.id.movies_list)
        loadingSection = fragmentView.findViewById(R.id.movies_loadingSection)
        noMoviesLabel = fragmentView.findViewById(R.id.movies_noMoviesLabel)
    }

    private fun setObservers() {
        viewModel.movies.observe(this, { updateView(it) })
    }

    private fun updateView(movies: List<Movie>) {
        loadingSection.visibility = View.INVISIBLE
        if (movies.isEmpty()) {
            moviesList.visibility = View.INVISIBLE
            noMoviesLabel.visibility = View.VISIBLE
        } else {
            if (viewModel.itemDeleted.value == true) {
                moviesAdapter.dataSetChanged(movies)
            } else {
                moviesList.layoutManager =
                    LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
                moviesAdapter = MoviesAdapter(requireContext(), movies, this)
                moviesList.adapter = moviesAdapter
            }
        }
    }

}