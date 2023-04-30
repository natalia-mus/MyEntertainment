package com.example.myentertainment

import com.google.firebase.database.DatabaseReference
import javax.inject.Inject
import javax.inject.Named

/**
 * Util provides moving existing records in database in case when database structure is modified
 */

class DatabaseRebuilder {

    init {
        BaseApplication.baseApplicationComponent.inject(this)
    }

    @Inject
    @Named("entertainmentReference")
    lateinit var entertainmentReference: DatabaseReference

    @Inject
    @Named("usersReference")
    lateinit var usersReference: DatabaseReference


    fun rebuild() {
        // fetching all records from rebuilding path
        usersReference.get().addOnSuccessListener {
            val userObjectsSet = it.value as HashMap<String, HashMap<String, Any>>

            for (userObject in userObjectsSet) {
                val userId = userObject.key

                // books:
                if (userObject.value.containsKey("books")) {
                    val objectsToMove = userObject.value["books"] as ArrayList<Any>

                    for ((id, book) in objectsToMove.withIndex()) {
                        // adding book to the new path
                        entertainmentReference.child(userId).child("books").child(id.toString()).setValue(book)
                    }

                    // deleting all books from the previous path
                    usersReference.child(userId).child("books").removeValue()
                }


                // movies:
                if (userObject.value.containsKey("movies")) {
                    val objectsToMove = userObject.value["movies"] as ArrayList<Any>

                    for ((id, movie) in objectsToMove.withIndex()) {
                        // adding movie to the new path
                        entertainmentReference.child(userId).child("movies").child(id.toString()).setValue(movie)
                    }

                    // deleting all movies from the previous path
                    usersReference.child(userId).child("movies").removeValue()
                }


                // games:
                if (userObject.value.containsKey("games")) {
                    val objectsToMove = userObject.value["games"] as ArrayList<Any>

                    for ((id, game) in objectsToMove.withIndex()) {
                        // adding game to the new path
                        entertainmentReference.child(userId).child("games").child(id.toString()).setValue(game)
                    }

                    // deleting all games from the previous path
                    usersReference.child(userId).child("games").removeValue()
                }


                // music:
                if (userObject.value.containsKey("music")) {
                    val objectsToMove = userObject.value["music"] as ArrayList<Any>

                    for ((id, song) in objectsToMove.withIndex()) {
                        // adding song to the new path
                        entertainmentReference.child(userId).child("music").child(id.toString()).setValue(song)
                    }

                    // deleting all songs from the previous path
                    usersReference.child(userId).child("music").removeValue()
                }
            }

        }
    }
}