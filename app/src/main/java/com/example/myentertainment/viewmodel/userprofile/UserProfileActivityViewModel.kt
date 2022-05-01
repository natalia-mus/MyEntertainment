package com.example.myentertainment.viewmodel.userprofile

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.myentertainment.BaseApplication
import com.example.myentertainment.Constants
import com.example.myentertainment.data.UserProfile
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import javax.inject.Inject

class UserProfileActivityViewModel : ViewModel() {

    init {
        BaseApplication.baseApplicationComponent.inject(this)
    }

    @Inject
    lateinit var databaseAuth: FirebaseAuth

    @Inject
    lateinit var databaseReference: DatabaseReference

    private val user = databaseAuth.uid.toString()

    val userProfile = MutableLiveData<UserProfile>()


    fun getUserProfileData() {
        databaseReference.child(user).child(Constants.USER_PROFILE_DATA).get()
            .addOnSuccessListener {
                userProfile.value = it.getValue(UserProfile::class.java)
            }
    }

    fun updateUserProfileData(userProfileData: UserProfile) {
        val data = hashMapOf<String, Any>(Constants.USER_PROFILE_DATA to userProfileData)
        databaseReference.child(user).updateChildren(data)
    }
}