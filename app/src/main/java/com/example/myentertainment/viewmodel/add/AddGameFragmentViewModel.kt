package com.example.myentertainment.viewmodel.add

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.myentertainment.BaseApplication
import com.example.myentertainment.`object`.CategoryObject
import com.example.myentertainment.`object`.ValidationResult
import com.example.myentertainment.data.Game
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import java.util.*
import javax.inject.Inject
import javax.inject.Named

class AddGameFragmentViewModel : ViewModel() {

    private val user: String
    private val path: DatabaseReference

    init {
        BaseApplication.baseApplicationComponent.inject(this)
        user = databaseAuth.uid.toString()
        path = entertainmentReference.child(user).child(CategoryObject.GAMES)
    }

    @Inject
    lateinit var databaseAuth: FirebaseAuth

    @Inject
    @Named("entertainmentReference")
    lateinit var entertainmentReference: DatabaseReference

    val loading = MutableLiveData<Boolean>()
    val game = MutableLiveData<Game>()
    val validationResult = MutableLiveData<ValidationResult>()
    val addingToDatabaseResult = MutableLiveData<Boolean>()


    fun addToDatabase(item: Game) {
        loading.value = true

        val itemId = UUID.randomUUID().toString()
        val title = item.title
        val releaseYear = item.releaseYear
        val genre = item.genre
        val rating = item.rating

        val game = Game(itemId, title, releaseYear, genre, rating)

        if (validation(game)) {
            path.child(itemId).setValue(game)
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

    fun getGame(id: String) {
        entertainmentReference.get().addOnSuccessListener {
            game.value = it.child(id).getValue(Game::class.java)
        }
    }

    fun updateItem(item: Game) {
        loading.value = true

        if (validation(item)) {
            val game = hashMapOf<String, Any>(item.id.toString() to item)
            path.updateChildren(game).addOnCompleteListener { task ->
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

    private fun validation(game: Game): Boolean {
        return if (game.title.isNullOrEmpty()) {
            loading.value = false
            validationResult.value = ValidationResult.EMPTY_VALUES
            false

        } else true
    }

}