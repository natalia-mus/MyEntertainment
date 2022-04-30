package com.example.myentertainment.viewmodel.add

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.myentertainment.BaseApplication
import com.example.myentertainment.Constants
import com.example.myentertainment.`object`.CategoryObject
import com.example.myentertainment.`object`.ValidationObject
import com.example.myentertainment.data.Game
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import javax.inject.Inject

class AddGameFragmentViewModel : ViewModel() {

    private val user: String
    private val mainPath: DatabaseReference
    private var itemId: String = "0"

    init {
        BaseApplication.baseApplicationComponent.inject(this)
        user = databaseAuth.uid.toString()
        mainPath = databaseReference.child(user).child(CategoryObject.GAMES)
        setItemId()
    }

    @Inject
    lateinit var databaseAuth: FirebaseAuth

    @Inject
    lateinit var databaseReference: DatabaseReference

    val loading = MutableLiveData<Boolean>()
    val validationResult = MutableLiveData<Int>()
    val addingToDatabaseResult = MutableLiveData<Boolean>()


    fun addToDatabase(item: Game) {
        loading.value = true

        val title = item.title
        val releaseYear = item.releaseYear
        val genre = item.genre
        val rating = item.rating

        val game = Game(itemId, title, releaseYear, genre, rating)

        if (validation(game)) {
            mainPath.child(itemId).setValue(game)
                .addOnCompleteListener() { task ->
                    if (task.isSuccessful) {
                        loading.value = false
                        addingToDatabaseResult.value = true
                    } else {
                        loading.value = false
                        addingToDatabaseResult.value = false
                    }
                }
        }
    }

    private fun setItemId() {
        mainPath
            .addValueEventListener(object : ValueEventListener {
                override fun onCancelled(error: DatabaseError) {}

                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        val childrenCount = snapshot.childrenCount
                        itemId = childrenCount.toString()

                        for (i in 0 until childrenCount) {
                            val child = snapshot.child(i.toString()).value
                            if (child.toString() == Constants.NULL) itemId = i.toString()
                        }
                    }
                }
            })
    }

    private fun validation(game: Game): Boolean {
        return if (game.title.isNullOrEmpty()) {
            loading.value = false
            validationResult.value = ValidationObject.EMPTY_VALUES
            false
        } else true
    }
}