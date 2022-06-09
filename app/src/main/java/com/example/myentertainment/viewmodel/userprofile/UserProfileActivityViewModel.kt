package com.example.myentertainment.viewmodel.userprofile

import android.net.Uri
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
import com.google.firebase.storage.UploadTask
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
    val updatingUserProfileDataSuccessful = MutableLiveData<Boolean>()
    val databaseTaskExecutionSuccessful = MutableLiveData<Boolean>()
    val profilePicture = MutableLiveData<Uri>()


    private fun profilePictureReference() : StorageReference {
        val path = StoragePathObject.PATH_PROFILE_PICTURES + "/" + user
        return storageReference.child(path)
    }

    fun getUserProfileData() {
        loading.value = true
        databaseReference.child(user).child(Constants.USER_PROFILE_DATA).get()
            .addOnCompleteListener() { task ->
                if (task.isSuccessful) {
                    userProfile.value = task.result?.getValue(UserProfile::class.java)
                    getProfilePictureUrl()
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
                    updatingUserProfileDataSuccessful.value = true
                } else {
                    loading.value = false
                    updatingUserProfileDataSuccessful.value = false
                }
            }
        }
    }

    fun changeProfilePicture(file: ByteArray) {
        loading.value = true
        val uploadTask = profilePictureReference().putBytes(file)
        changeProfilePicture(uploadTask)
    }

    fun changeProfilePicture(file: Uri) {
        loading.value = true
        val uploadTask = profilePictureReference().putFile(file)
        changeProfilePicture(uploadTask)
    }

    private fun changeProfilePicture(uploadTask: UploadTask) {
        uploadTask.addOnCompleteListener() { task ->
            if (task.isSuccessful) {
                getProfilePictureUrl()
            } else {
                loading.value = false
                databaseTaskExecutionSuccessful.value = false
            }
        }
    }

    private fun getProfilePictureUrl() {
        profilePictureReference().downloadUrl.addOnCompleteListener() { task ->
            if (task.isSuccessful) {
                loading.value = false
                profilePicture.value = task.result
            } else {
                loading.value = false
                databaseTaskExecutionSuccessful.value = false
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