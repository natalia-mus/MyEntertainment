package com.example.myentertainment.view

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.myentertainment.R
import com.example.myentertainment.viewmodel.FindFriendsViewModel

class FindFriendsActivity : AppCompatActivity() {

    private lateinit var viewModel: FindFriendsViewModel

    private lateinit var searchField: TextView
    private lateinit var searchButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_find_friends)

        viewModel = ViewModelProvider(this).get(FindFriendsViewModel::class.java)
        initView()
    }

    private fun findFriends() {
        val phrase = searchField.text
        viewModel.findFriends(phrase)
    }

    private fun initView() {
        searchField = findViewById(R.id.findFriends_searchField)
        searchButton = findViewById(R.id.findFriends_searchButton)

        searchButton.setOnClickListener {
            findFriends()
        }
    }

}