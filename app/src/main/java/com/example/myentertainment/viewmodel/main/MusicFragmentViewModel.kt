package com.example.myentertainment.viewmodel.main

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.myentertainment.BaseApplication
import com.example.myentertainment.`object`.CategoryObject
import com.example.myentertainment.data.Music
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import javax.inject.Inject
import javax.inject.Named

class MusicFragmentViewModel : ViewModel() {

    private val mainPath: DatabaseReference
    private val user: String

    init {
        BaseApplication.baseApplicationComponent.inject(this)
        user = databaseAuth.uid.toString()
        mainPath = entertainmentReference.child(user).child(CategoryObject.MUSIC)
    }

    @Inject
    lateinit var databaseAuth: FirebaseAuth

    @Inject
    @Named("entertainmentReference")
    lateinit var entertainmentReference: DatabaseReference

    val music = MutableLiveData<List<Music>>()
    val itemDeleted = MutableLiveData<Boolean>(false)


    fun deleteMusic(id: String?) {
        itemDeleted.value = false
        mainPath.child(id.toString()).removeValue()
            .addOnCompleteListener() { task ->
                if (task.isSuccessful) {
                    fetchMusic()
                    itemDeleted.value = true
                } else {
                    Log.e("MusicFragmentViewModel", "error")
                }
            }
    }

    fun fetchMusic() {
        mainPath.get().addOnSuccessListener {
            val musicList: MutableList<Music> = mutableListOf()
            var lastChild = it.childrenCount

            var i = 0
            while (i < lastChild) {
                if (!it.child(i.toString()).exists()) {
                    lastChild++
                }
                i++
            }

            for (j in 0 until lastChild) {
                val singleMusic = it.child(j.toString()).getValue(Music::class.java)
                singleMusic?.let { music -> musicList.add(music) }
            }

            music.value = musicList
        }
    }

}