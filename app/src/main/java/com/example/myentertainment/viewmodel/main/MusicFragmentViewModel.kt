package com.example.myentertainment.viewmodel.main

import androidx.lifecycle.MutableLiveData
import com.example.myentertainment.`object`.CategoryObject
import com.example.myentertainment.data.Music

class MusicFragmentViewModel : EntertainmentViewModel(CategoryObject.MUSIC) {

    val music = MutableLiveData<List<Music>>()
    val itemDeleted = MutableLiveData<Boolean>(false)


    fun deleteMusic(id: String?) {
        itemDeleted.value = false
//        path.child(id.toString()).removeValue()
//            .addOnCompleteListener() { task ->
//                if (task.isSuccessful) {
//                    fetchMusic()
//                    itemDeleted.value = true
//                } else {
//                    Log.e("MusicFragmentViewModel", "error")
//                }
//            }
    }

    fun fetchMusic() {
//        val result = ArrayList<IEntertainment>()
//
//        path.get().addOnSuccessListener {
//            it.children.forEach {
//                val child = it.getValue(Music::class.java)
//                child?.let { music -> result.add(music) }
//            }
//
//            //music.value = orderByCreationDate(result) as ArrayList<Music>
//        }

        fetchItems()
    }

    override fun onItemsValueChanged() {
        music.value = items.value as ArrayList<Music>
    }

}