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

    private lateinit var searchSection: ConstraintLayout
    private lateinit var searchField: TextView
    private lateinit var searchButton: ImageButton
    private lateinit var loadingSection: ConstraintLayout
    private lateinit var noResultsInfo: TextView
    private lateinit var usersList: RecyclerView

    private var userId: String? = null
    private var friendsListChanged = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_friends)

        viewModel = ViewModelProvider(this).get(FriendsViewModel::class.java)
        initView()
        setObservers()
        setUserId()
        getFriends()
        setFriendsListener()
    }

    override fun onResume() {
        super.onResume()
        refreshFriendsList()
    }

    override fun onUserTileClicked(userId: String?) {
        val intent = Intent(this, UserProfileActivity::class.java)
        if (userId != null) {
            intent.putExtra(Constants.USER_ID, userId)
        }
        startActivity(intent)
    }

    private fun enableSearchSection(enable: Boolean) {
        searchField.isEnabled = enable
        searchButton.isEnabled = enable
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
        searchSection = findViewById(R.id.friends_searchSection)
        searchField = findViewById(R.id.friends_searchField)
        searchButton = findViewById(R.id.friends_searchButton)
        loadingSection = findViewById(R.id.friends_loadingSection)
        noResultsInfo = findViewById(R.id.friends_noResultsInfo)
        usersList = findViewById(R.id.friends_list)

        searchButton.setOnClickListener {
            findFriends()
            Utils.hideKeyboard(this)
        }

        searchField.setOnEditorActionListener { _, _, _ ->
            findFriends()
            Utils.hideKeyboard(this)
            true
        }
    }

    private fun refreshFriendsList() {
        if (friendsListChanged) {
            getFriends()
            viewModel.onFriendsListRefreshed()
        }
    }

    private fun setFriendsListener() {
        viewModel.setFriendsListener(userId)
    }

    private fun setObservers() {
        viewModel.loading.observe(this) { updateLoadingSection(it) }
        viewModel.status.observe(this) { updateStatusInfo(it) }
        viewModel.friendsListChanged.observe(this) { updateFriendsListChanged(it) }
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

    private fun updateFriendsListChanged(changed: Boolean) {
        friendsListChanged = changed
    }

    private fun updateLoadingSection(loading: Boolean) {
        if (loading) {
            loadingSection.visibility = View.VISIBLE
            noResultsInfo.visibility = View.GONE
            enableSearchSection(false)
        } else {
            loadingSection.visibility = View.GONE
            enableSearchSection(true)
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
                noResultsInfo.text = resources.getString(R.string.no_results)
                noResultsInfo.visibility = View.VISIBLE
            }
            SearchUsersStatus.NO_FRIENDS -> {
                noResultsInfo.text = resources.getString(R.string.you_have_no_friends_yet)
                noResultsInfo.visibility = View.VISIBLE
                searchSection.visibility = View.GONE
            }
            SearchUsersStatus.ERROR -> {
                usersList.visibility = View.GONE
                noResultsInfo.visibility = View.VISIBLE
                Toast.makeText(this, getString(R.string.error_try_again), Toast.LENGTH_LONG).show()
            }
        }
    }

}