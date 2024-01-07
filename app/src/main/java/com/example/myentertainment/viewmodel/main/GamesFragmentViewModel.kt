package com.example.myentertainment.viewmodel.main

import androidx.lifecycle.MutableLiveData
import com.example.myentertainment.`object`.CategoryObject
import com.example.myentertainment.data.Game

class GamesFragmentViewModel : EntertainmentViewModel(CategoryObject.GAMES) {

    val games = MutableLiveData<List<Game>>()
    val itemDeleted = MutableLiveData<Boolean>(false)


    fun deleteGame(id: String?) {
        itemDeleted.value = false
//        path.child(id.toString()).removeValue().addOnCompleteListener() { task ->
//            if (task.isSuccessful) {
//                fetchGames()
//                itemDeleted.value = true
//            } else {
//                Log.e("GamesFragmentViewModel", "error")
//            }
//        }
    }

    fun fetchGames() {
//        val result = ArrayList<IEntertainment>()
//
//        path.get().addOnSuccessListener {
//            it.children.forEach {
//                val child = it.getValue(Game::class.java)
//                child?.let { game -> result.add(game) }
//            }
//
//            //games.value = orderByCreationDate(result) as ArrayList<Game>
//        }

        fetchItems()
    }

    override fun onItemsValueChanged() {
        games.value = items.value as ArrayList<Game>
    }

}