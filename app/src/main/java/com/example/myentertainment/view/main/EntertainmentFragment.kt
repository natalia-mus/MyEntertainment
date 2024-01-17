package com.example.myentertainment.view.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myentertainment.R
import com.example.myentertainment.`object`.CategoryObject
import com.example.myentertainment.data.*
import com.example.myentertainment.interfaces.OnItemClickAction
import com.example.myentertainment.view.main.adapters.*
import com.example.myentertainment.viewmodel.main.EntertainmentViewModel

class EntertainmentFragment(val category: CategoryObject) : Fragment(), OnItemClickAction {

    private lateinit var fragmentView: View
    private lateinit var viewModel: EntertainmentViewModel

    private lateinit var itemsList: RecyclerView
    private lateinit var itemsAdapter: IEntertainmentAdapter
    private lateinit var loadingSection: ConstraintLayout
    private lateinit var noItemsLabel: TextView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        fragmentView = inflater.inflate(getLayoutId(), container, false)
        viewModel = EntertainmentViewModel.create(category, this)
        initView()
        setObservers()
        viewModel.fetchItems()
        return fragmentView
    }

    override fun onItemClicked(id: String?) {
        //editBook(id)
    }

    override fun onItemLongClicked(id: String?) {
        viewModel.deleteItem(id)
    }

    private fun getLayoutId(): Int {
        return when (category) {
            CategoryObject.MOVIES -> R.layout.fragment_movies
            CategoryObject.BOOKS -> R.layout.fragment_books
            CategoryObject.GAMES -> R.layout.fragment_games
            CategoryObject.MUSIC -> R.layout.fragment_music
        }
    }

    private fun initView() {
        when (category) {
            CategoryObject.MOVIES -> {
                itemsList = fragmentView.findViewById(R.id.movies_list)
                loadingSection = fragmentView.findViewById(R.id.movies_loadingSection)
                noItemsLabel = fragmentView.findViewById(R.id.movies_noMoviesLabel)
            }
            CategoryObject.BOOKS -> {
                itemsList = fragmentView.findViewById(R.id.books_list)
                loadingSection = fragmentView.findViewById(R.id.books_loadingSection)
                noItemsLabel = fragmentView.findViewById(R.id.books_noBooksLabel)
            }
            CategoryObject.GAMES -> {
                itemsList = fragmentView.findViewById(R.id.games_list)
                loadingSection = fragmentView.findViewById(R.id.games_loadingSection)
                noItemsLabel = fragmentView.findViewById(R.id.games_noGamesLabel)
            }
            CategoryObject.MUSIC -> {
                itemsList = fragmentView.findViewById(R.id.music_list)
                loadingSection = fragmentView.findViewById(R.id.music_loadingSection)
                noItemsLabel = fragmentView.findViewById(R.id.music_noMusicLabel)
            }
        }
    }

    private fun setObservers() {
        viewModel.entertainmentList.observe(this) { updateView(it as List<IEntertainment>) }
    }

    private fun updateView(items: List<IEntertainment>) {
        loadingSection.visibility = View.GONE
        if (items.isEmpty()) {
            itemsList.visibility = View.GONE
            noItemsLabel.visibility = View.VISIBLE
        } else {
            if (viewModel.itemDeleted.value == true) {
                itemsAdapter.dataSetChanged(items)
            } else {
                setAdapter(items)
                itemsList.adapter = itemsAdapter as RecyclerView.Adapter<*>
            }
        }
    }

    private fun setAdapter(items: List<IEntertainment>) {
        itemsList.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        itemsAdapter = when (category) {
            CategoryObject.MOVIES -> MoviesAdapter(requireContext(), items as List<Movie>, this)
            CategoryObject.BOOKS -> BooksAdapter(requireContext(), items as List<Book>, this)
            CategoryObject.GAMES -> GamesAdapter(requireContext(), items as List<Game>, this)
            CategoryObject.MUSIC -> MusicAdapter(requireContext(), items as List<Music>, this)
        }
    }

}