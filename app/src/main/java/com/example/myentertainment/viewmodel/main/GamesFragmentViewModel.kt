package com.example.myentertainment.viewmodel.main

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.myentertainment.BaseApplication
import com.example.myentertainment.`object`.CategoryObject
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
    val itemDeleted = MutableLiveData<Boolean>(false)


    fun fetchGames() {
        databaseReference.child(user).child(CategoryObject.GAMES).get().addOnSuccessListener {
            val gamesList: MutableList<Game> = mutableListOf()
            var lastChild = it.childrenCount

            var i = 0
            while (i < lastChild) {
                if (!it.child(i.toString()).exists()) {
                    lastChild++
                }
                i++
            }

            for (j in 0 until lastChild) {
                val singleGame = it.child(j.toString()).getValue(Game::class.java)
                singleGame?.let { game -> gamesList.add(game) }
            }

            games.value = gamesList
        }
    }


    fun deleteGame(id: String?) {
        itemDeleted.value = false
        databaseReference.child(user).child(CategoryObject.GAMES).child(id.toString()).removeValue()
            .addOnCompleteListener() { task ->
                if (task.isSuccessful) {
                    fetchGames()
                    itemDeleted.value = true
                } else {
                    Log.e("error", "error")
                }
            }
    }
}