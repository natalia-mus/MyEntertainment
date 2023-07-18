package com.example.myentertainment.viewmodel

import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.myentertainment.BaseApplication
import com.example.myentertainment.`object`.StoragePathObject
import com.example.myentertainment.data.UserProfile
import com.example.myentertainment.data.UserProfileData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.storage.StorageException
import com.google.firebase.storage.StorageReference
import javax.inject.Inject
import javax.inject.Named

open class UserProfileViewModel : ViewModel() {

    init {
        BaseApplication.baseApplicationComponent.inject(this)
    }

    @Inject
    lateinit var databaseAuth: FirebaseAuth

    @Inject
    lateinit var storageReference: StorageReference

    @Inject
    @Named("usersReference")
    lateinit var usersReference: DatabaseReference


    val updatingProfilePictureSuccessful = MutableLiveData<Boolean>()
    val user = databaseAuth.uid.toString()
    val userProfiles = MutableLiveData<ArrayList<UserProfile>>()

    private var userProfileData: UserProfileData? = null
    private var profilePicture: Uri? = null
    private val userProfilesArray = ArrayList<UserProfile>()

    private var requests = 0


    fun getUserProfile(userId: String?) {
        getUserProfileData(userId)
    }

    fun getUserProfileData(userId: String?) {
        val userIds = ArrayList<String?>()
        userIds.add(userId)
        getUserProfiles(userIds)
    }

    fun getUserProfiles(userIds: ArrayList<String?>) {
        for (userId in userIds) {
            requests++
            val id = userId ?: user

            usersReference.child(id).get().addOnCompleteListener() { task ->
                if (task.isSuccessful) {
                    userProfileData = task.result?.getValue(UserProfileData::class.java)
                    getProfilePictureUrl(id)

                } else {
                    requests--
                    userProfileData = null
                }
            }
        }
    }

    fun getProfilePictureUrl(id: String) {
        profilePictureReference(id).downloadUrl
            .addOnSuccessListener {
                if (requests > 0) requests--
                profilePicture = it
                setUserProfileValue()

            }.addOnFailureListener {
                val errorCode = (it as StorageException).errorCode

                if (errorCode == StorageException.ERROR_OBJECT_NOT_FOUND) {
                    if (requests > 0) requests--
                    profilePicture = null
                    setUserProfileValue()
                } else {
                    updatingProfilePictureSuccessful.value = false
                }
            }
    }

    fun profilePictureReference(id: String): StorageReference {
        val path = StoragePathObject.PATH_PROFILE_PICTURES + "/" + id
        return storageReference.child(path)
    }

    private fun setUserProfileValue() {
        if (userProfileData != null && profilePicture != null) {
            userProfilesArray.add(UserProfile(userProfileData, profilePicture))

            if (requests == 0) {
                userProfiles.value = userProfilesArray
            }
        }
    }
}