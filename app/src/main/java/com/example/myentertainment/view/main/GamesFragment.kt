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
import com.example.myentertainment.data.Game
import com.example.myentertainment.interfaces.OnItemClickAction
import com.example.myentertainment.view.add.AddActivity
import com.example.myentertainment.view.main.adapters.GamesAdapter
import com.example.myentertainment.viewmodel.main.GamesFragmentViewModel

class GamesFragment : Fragment(), OnItemClickAction {

    private lateinit var fragmentView: View
    private lateinit var viewModel: GamesFragmentViewModel

    private lateinit var gamesList: RecyclerView
    private lateinit var gamesAdapter: GamesAdapter
    private lateinit var loadingSection: ConstraintLayout
    private lateinit var noGamesLabel: TextView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        fragmentView = inflater.inflate(R.layout.fragment_games, container, false)
        initView()
        viewModel = ViewModelProvider(this).get(GamesFragmentViewModel::class.java)
        setObservers()
        viewModel.fetchGames()
        return fragmentView
    }

    private fun initView() {
        gamesList = fragmentView.findViewById(R.id.games_list)
        loadingSection = fragmentView.findViewById(R.id.games_loadingSection)
        noGamesLabel = fragmentView.findViewById(R.id.games_noGamesLabel)
    }

    private fun setObservers() {
        viewModel.games.observe(this, { updateView(it) })
    }

    private fun updateView(games: List<Game>) {
        loadingSection.visibility = View.INVISIBLE
        if (games.isEmpty()) {
            gamesList.visibility = View.INVISIBLE
            noGamesLabel.visibility = View.VISIBLE
        } else {
            if (viewModel.itemDeleted.value == true) {
                gamesAdapter.dataSetChanged(games)
            } else {
                gamesList.layoutManager =
                    LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
                gamesAdapter = GamesAdapter(requireContext(), games, this)
                gamesList.adapter = gamesAdapter
            }
        }
    }

    private fun editGame(id: String?) {
        val intent = Intent(activity, AddActivity::class.java)
        intent.putExtra(Constants.CATEGORY, CategoryObject.GAMES)
        intent.putExtra(Constants.ID, id!!)
        startActivity(intent)
    }

    override fun onItemClicked(id: String?) {
        editGame(id)
    }

    override fun onItemLongClicked(id: String?) {
        viewModel.deleteGame(id)
    }
}