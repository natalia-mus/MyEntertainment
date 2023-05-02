package com.example.myentertainment.view

import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.ViewModelProvider
import com.example.myentertainment.R
import com.example.myentertainment.data.UserProfile
import com.example.myentertainment.viewmodel.FindFriendsViewModel
import com.example.myentertainment.viewmodel.SearchUsersStatus

class FindFriendsActivity : AppCompatActivity() {

    private lateinit var viewModel: FindFriendsViewModel

    private lateinit var searchField: TextView
    private lateinit var searchButton: ImageButton
    private lateinit var loadingSection: ConstraintLayout
    private lateinit var noResultsInfo: TextView

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

        searchButton.setOnClickListener {
            findFriends()
        }
    }

    private fun setObservers() {
        viewModel.loading.observe(this) { updateLoadingSection(it) }
        viewModel.status.observe(this) { updateStatusInfo(it) }
        viewModel.results.observe(this) { showResults(it) }
    }

    private fun showResults(results: ArrayList<UserProfile>) {
        // TODO
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
            SearchUsersStatus.SUCCESS -> noResultsInfo.visibility = View.GONE
            SearchUsersStatus.NO_RESULTS -> noResultsInfo.visibility = View.VISIBLE
            SearchUsersStatus.ERROR -> {
                noResultsInfo.visibility = View.VISIBLE
                Toast.makeText(this, getString(R.string.error), Toast.LENGTH_LONG).show()
            }
        }
    }

}