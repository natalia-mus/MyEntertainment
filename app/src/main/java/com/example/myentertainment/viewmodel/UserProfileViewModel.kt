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


    val loading = MutableLiveData<Boolean>()
    val updatingProfilePictureSuccessful = MutableLiveData<Boolean>()
    val user = databaseAuth.uid.toString()
    val userProfile = MutableLiveData<UserProfile>()

    private val userProfileData = MutableLiveData<UserProfileData?>()
    private val profilePicture = MutableLiveData<Uri?>()


    fun getUserProfile(userId: String?) {
        getUserProfileData(userId, true)
    }

    fun getUserProfileData(userId: String?, getUserProfilePicture: Boolean = false) {
        loading.value = true
        val id = userId ?: user

        usersReference.child(id).get().addOnCompleteListener() { task ->
            if (task.isSuccessful) {
                userProfileData.value = task.result?.getValue(UserProfileData::class.java)
                if (getUserProfilePicture) {
                    getProfilePictureUrl(id)
                } else {
                    setUserProfileValue()
                }

            } else {
                loading.value = false
                userProfileData.value = null
            }
        }
    }

    private fun setUserProfileValue() {
        userProfile.value = UserProfile(userProfileData.value, profilePicture.value)
    }

    fun getProfilePictureUrl(id: String) {
        profilePictureReference(id).downloadUrl
            .addOnSuccessListener {
                loading.value = false
                profilePicture.value = it
                setUserProfileValue()

            }.addOnFailureListener {
                loading.value = false
                val errorCode = (it as StorageException).errorCode

                if (errorCode == StorageException.ERROR_OBJECT_NOT_FOUND) {
                    profilePicture.value = null
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
}