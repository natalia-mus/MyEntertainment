package com.example.myentertainment.viewmodel.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.myentertainment.BaseApplication
import com.example.myentertainment.`object`.CategoryObject
import com.example.myentertainment.data.Music
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import javax.inject.Inject

class MusicFragmentViewModel : ViewModel() {

    init {
        BaseApplication.baseApplicationComponent.inject(this)
    }

    @Inject
    lateinit var databaseAuth: FirebaseAuth

    @Inject
    lateinit var databaseReference: DatabaseReference

    private val user = databaseAuth.uid.toString()

    val music = MutableLiveData<List<Music>>()


    fun fetchMusic() {
        databaseReference.child(user).child(CategoryObject.MUSIC).get().addOnSuccessListener {
            val countItems = it.childrenCount
            val musicList: MutableList<Music> = mutableListOf()

            for (i in 0 until countItems) {
                val singleMusic = it.child(i.toString()).getValue(Music::class.java)
                singleMusic?.let { music -> musicList.add(music) }
            }
            music.value = musicList
        }
    }
}