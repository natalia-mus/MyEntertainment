package com.example.myentertainment.viewmodel.main

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.myentertainment.BaseApplication
import com.example.myentertainment.`object`.CategoryObject
import com.example.myentertainment.data.IEntertainment
import com.example.myentertainment.data.Music
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import javax.inject.Inject
import javax.inject.Named

class MusicFragmentViewModel : ViewModel() {

    private val path: DatabaseReference
    private val user: String

    init {
        BaseApplication.baseApplicationComponent.inject(this)
        user = databaseAuth.uid.toString()
        path = entertainmentReference.child(user).child(CategoryObject.MUSIC.categoryName)
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
        path.child(id.toString()).removeValue()
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
        val result = ArrayList<IEntertainment>()

        path.get().addOnSuccessListener {
            it.children.forEach {
                val child = it.getValue(Music::class.java)
                child?.let { music -> result.add(music) }
            }

            //music.value = orderByCreationDate(result) as ArrayList<Music>
        }
    }

}