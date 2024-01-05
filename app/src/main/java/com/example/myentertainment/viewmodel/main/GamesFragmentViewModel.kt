package com.example.myentertainment.viewmodel.main

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.myentertainment.BaseApplication
import com.example.myentertainment.`object`.CategoryObject
import com.example.myentertainment.data.Game
import com.example.myentertainment.data.IEntertainment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import javax.inject.Inject
import javax.inject.Named

class GamesFragmentViewModel : EntertainmentViewModel(CategoryObject.GAMES) {

    private val path: DatabaseReference
    private val user: String

    init {
        BaseApplication.baseApplicationComponent.inject(this)
        user = databaseAuth.uid.toString()
        path = entertainmentReference.child(user).child(CategoryObject.GAMES.categoryName)
    }

//    @Inject
//    lateinit var databaseAuth: FirebaseAuth
//
//    @Inject
//    @Named("entertainmentReference")
//    lateinit var entertainmentReference: DatabaseReference

    val games = MutableLiveData<List<Game>>()
    val itemDeleted = MutableLiveData<Boolean>(false)


    fun deleteGame(id: String?) {
        itemDeleted.value = false
        path.child(id.toString()).removeValue().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                fetchGames()
                itemDeleted.value = true
            } else {
                Log.e("GamesFragmentViewModel", "error")
            }
        }
    }

    fun fetchGames() {
        val result = ArrayList<IEntertainment>()

        path.get().addOnSuccessListener {
            it.children.forEach {
                val child = it.getValue(Game::class.java)
                child?.let { game -> result.add(game) }
            }

            //games.value = orderByCreationDate(result) as ArrayList<Game>
        }
    }

}