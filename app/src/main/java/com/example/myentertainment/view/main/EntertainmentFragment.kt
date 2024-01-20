package com.example.myentertainment.view.main

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myentertainment.Constants
import com.example.myentertainment.R
import com.example.myentertainment.`object`.CategoryObject
import com.example.myentertainment.data.*
import com.example.myentertainment.interfaces.OnItemClickAction
import com.example.myentertainment.view.add.AddActivity
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
        fragmentView = inflater.inflate(R.layout.fragment_entertainment, container, false)
        viewModel = EntertainmentViewModel.create(category, this)
        initView()
        setObservers()
        viewModel.fetchItems()
        return fragmentView
    }

    override fun onItemClicked(id: String?) {
        editItem(id)
    }

    override fun onItemLongClicked(id: String?) {
        viewModel.deleteItem(id)
    }

    private fun editItem(id: String?) {
        if (id != null) {
            val intent = Intent(activity, AddActivity::class.java)
            intent.putExtra(Constants.CATEGORY, category.categoryName)
            intent.putExtra(Constants.ID, id)
            startActivity(intent)
        }
    }

    private fun initView() {
        itemsList = fragmentView.findViewById(R.id.entertainment_itemsList)
        loadingSection = fragmentView.findViewById(R.id.entertainment_loadingSection)
        noItemsLabel = fragmentView.findViewById(R.id.entertainment_noItemsLabel)

        val entertainmentFragment = fragmentView.findViewById<ConstraintLayout>(R.id.entertainment_fragment)
        when (category) {
            CategoryObject.MOVIES -> {
                entertainmentFragment.background = ResourcesCompat.getDrawable(resources, R.color.red, null)
                noItemsLabel.text = resources.getString(R.string.no_movies)
            }
            CategoryObject.BOOKS -> {
                entertainmentFragment.background = ResourcesCompat.getDrawable(resources, R.color.blue, null)
                noItemsLabel.text = resources.getString(R.string.no_books)
            }
            CategoryObject.GAMES -> {
                entertainmentFragment.background = ResourcesCompat.getDrawable(resources, R.color.green, null)
                noItemsLabel.text = resources.getString(R.string.no_games)
            }
            CategoryObject.MUSIC -> {
                entertainmentFragment.background = ResourcesCompat.getDrawable(resources, R.color.yellow, null)
                noItemsLabel.text = resources.getString(R.string.no_music)
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

}