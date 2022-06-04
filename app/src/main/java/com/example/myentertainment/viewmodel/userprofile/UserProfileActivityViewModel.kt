package com.example.myentertainment.viewmodel.userprofile

import android.net.Uri
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.myentertainment.BaseApplication
import com.example.myentertainment.Constants
import com.example.myentertainment.`object`.StoragePathObject
import com.example.myentertainment.`object`.ValidationObject
import com.example.myentertainment.data.UserProfile
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.storage.StorageReference
import javax.inject.Inject

class UserProfileActivityViewModel : ViewModel() {

    init {
        BaseApplication.baseApplicationComponent.inject(this)
    }

    @Inject
    lateinit var databaseAuth: FirebaseAuth

    @Inject
    lateinit var databaseReference: DatabaseReference

    @Inject
    lateinit var storageReference: StorageReference

    private val user = databaseAuth.uid.toString()

    val loading = MutableLiveData<Boolean>()
    val userProfile = MutableLiveData<UserProfile?>()
    val validationResult = MutableLiveData<Int>()
    val addingToDatabaseResult = MutableLiveData<Boolean>()


    fun getUserProfileData() {
        loading.value = true
        databaseReference.child(user).child(Constants.USER_PROFILE_DATA).get()
            .addOnCompleteListener() { task ->
                if (task.isSuccessful) {
                    loading.value = false
                    userProfile.value = task.result?.getValue(UserProfile::class.java)
                } else {
                    loading.value = false
                    userProfile.value = null
                }
            }
    }

    fun updateUserProfileData(userProfileData: UserProfile) {
        loading.value = true

        if (validation(userProfileData)) {
            val data = hashMapOf<String, Any>(Constants.USER_PROFILE_DATA to userProfileData)
            databaseReference.child(user).updateChildren(data).addOnCompleteListener() { task ->
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

    fun changeProfilePicture(file: Uri) {
        loading.value = true
        val path = StoragePathObject.PATH_PROFILE_PICTURES + "/" + user
        val reference = storageReference.child(path)
        val uploadTask = reference.putFile(file)

        uploadTask.addOnCompleteListener() { task ->
            if (task.isSuccessful) {
                loading.value = false
                Log.e("uploadTask", "success")
            } else {
                loading.value = false
                Log.e("uploadTask", "failure")
            }
        }
    }

    private fun validation(userProfileData: UserProfile): Boolean {
        return if (userProfileData.username.isNullOrEmpty()) {
            loading.value = false
            validationResult.value = ValidationObject.EMPTY_VALUES
            false
        } else true
    }
}