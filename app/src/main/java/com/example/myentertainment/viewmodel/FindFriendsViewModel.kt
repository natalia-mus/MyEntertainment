package com.example.myentertainment.viewmodel

import androidx.lifecycle.ViewModel
import com.google.firebase.database.DatabaseReference
import javax.inject.Inject
import javax.inject.Named

class FindFriendsViewModel: ViewModel() {

    @Inject
    @Named("usersReference")
    lateinit var databaseReference: DatabaseReference


    fun findFriends(phrase: String) {
        // TODO
    }
}
