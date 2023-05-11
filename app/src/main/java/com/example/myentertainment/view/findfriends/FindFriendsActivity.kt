package com.example.myentertainment.view.findfriends

import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myentertainment.R
import com.example.myentertainment.viewmodel.FindFriendsViewModel
import com.example.myentertainment.viewmodel.SearchUsersStatus

class FindFriendsActivity : AppCompatActivity() {

    private lateinit var viewModel: FindFriendsViewModel

    private lateinit var searchField: TextView
    private lateinit var searchButton: ImageButton
    private lateinit var loadingSection: ConstraintLayout
    private lateinit var noResultsInfo: TextView
    private lateinit var usersList: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_find_friends)

        viewModel = ViewModelProvider(this).get(FindFriendsViewModel::class.java)
        initView()
        setObservers()
    }

    private fun findFriends() {
        val phrase = searchField.text.toString()
        viewModel.findFriends(phrase)
    }

    private fun initView() {
        searchField = findViewById(R.id.findFriends_searchField)
        searchButton = findViewById(R.id.findFriends_searchButton)
        loadingSection = findViewById(R.id.findFriends_loadingSection)
        noResultsInfo = findViewById(R.id.findFriends_noResultsInfo)
        usersList = findViewById(R.id.findFriends_list)

        searchButton.setOnClickListener {
            findFriends()
        }
    }

    private fun setObservers() {
        viewModel.loading.observe(this) { updateLoadingSection(it) }
        viewModel.status.observe(this) { updateStatusInfo(it) }
    }

    private fun showResults() {
        val users = viewModel.users.value
        val profilePictures = viewModel.profilePictures.value

        if (users != null && profilePictures != null) {
            usersList.layoutManager = GridLayoutManager(this, 2)
            val adapter = FindFriendsAdapter(this, users, profilePictures)
            usersList.adapter = adapter

            usersList.visibility = View.VISIBLE
        }
    }

    private fun updateLoadingSection(loading: Boolean) {
        if (loading) {
            loadingSection.visibility = View.VISIBLE
        } else {
            loadingSection.visibility = View.GONE
        }
    }

    private fun updateStatusInfo(status: SearchUsersStatus) {
        when (status) {
            SearchUsersStatus.SUCCESS -> {
                noResultsInfo.visibility = View.GONE
                showResults()
            }
            SearchUsersStatus.NO_RESULTS -> {
                usersList.visibility = View.GONE
                noResultsInfo.visibility = View.VISIBLE
            }
            SearchUsersStatus.ERROR -> {
                usersList.visibility = View.GONE
                noResultsInfo.visibility = View.VISIBLE
                Toast.makeText(this, getString(R.string.error_try_again), Toast.LENGTH_LONG).show()
            }
        }
    }

}