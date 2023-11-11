package com.example.myentertainment.view.friends

import android.content.Intent
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
import com.example.myentertainment.Constants
import com.example.myentertainment.R
import com.example.myentertainment.Utils
import com.example.myentertainment.view.UserProfileActivity
import com.example.myentertainment.viewmodel.FriendsViewModel
import com.example.myentertainment.viewmodel.SearchUsersStatus

class FriendsActivity : AppCompatActivity(), UserTileClickListener {

    private lateinit var viewModel: FriendsViewModel

    private lateinit var searchField: TextView
    private lateinit var searchButton: ImageButton
    private lateinit var loadingSection: ConstraintLayout
    private lateinit var noResultsInfo: TextView
    private lateinit var usersList: RecyclerView

    private var userId: String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_find_friends)

        viewModel = ViewModelProvider(this).get(FriendsViewModel::class.java)
        initView()
        setObservers()
        setUserId()
        getFriends()
    }

    override fun onUserTileClicked(userId: String?) {
        val intent = Intent(this, UserProfileActivity::class.java)
        if (userId != null) {
            intent.putExtra(Constants.USER_ID, userId)
        }
        startActivity(intent)
    }

    private fun findFriends() {
        val phrase = searchField.text.toString()
        viewModel.findFriends(phrase, userId)
    }

    private fun getFriends() {
        if (userId != null) {
            viewModel.getFriends(userId!!)
        }
    }

    private fun initView() {
        searchField = findViewById(R.id.findFriends_searchField)
        searchButton = findViewById(R.id.findFriends_searchButton)
        loadingSection = findViewById(R.id.findFriends_loadingSection)
        noResultsInfo = findViewById(R.id.findFriends_noResultsInfo)
        usersList = findViewById(R.id.findFriends_list)

        searchButton.setOnClickListener {
            findFriends()
            Utils.hideKeyboard(this)
        }
    }

    private fun setObservers() {
        viewModel.loading.observe(this) { updateLoadingSection(it) }
        viewModel.status.observe(this) { updateStatusInfo(it) }
    }

    private fun setUserId() {
        if (intent.hasExtra(Constants.USER_ID)) {
            userId = intent.getStringExtra(Constants.USER_ID).toString()
        }
    }

    private fun showResults() {
        val users = viewModel.userProfiles.value

        if (users != null) {
            usersList.layoutManager = GridLayoutManager(this, 2)
            val adapter = FriendsListAdapter(this, users, this)
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
        loadingSection.visibility = View.GONE
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