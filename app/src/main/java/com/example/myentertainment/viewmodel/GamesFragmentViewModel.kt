package com.example.myentertainment.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.myentertainment.BaseApplication
import com.example.myentertainment.data.Game
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import javax.inject.Inject

class GamesFragmentViewModel : ViewModel() {

    init {
        BaseApplication.baseApplicationComponent.inject(this)
    }

    @Inject
    lateinit var databaseAuth: FirebaseAuth

    @Inject
    lateinit var databaseReference: DatabaseReference

    private val user = databaseAuth.uid.toString()

    val games = MutableLiveData<List<Game>>()


    fun fetchGames() {
        databaseReference.child(user).child("games").get().addOnSuccessListener {
            val countItems = it.childrenCount
            val gamesList: MutableList<Game> = mutableListOf()

            for (i in 0 until countItems) {
                val singleGame = it.child(i.toString()).getValue(Game::class.java)
                gamesList.add(singleGame!!)
            }
            games.value = gamesList
        }
    }
}