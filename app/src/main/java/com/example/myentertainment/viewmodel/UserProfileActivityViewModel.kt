package com.example.myentertainment.viewmodel

import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.myentertainment.BaseApplication
import com.example.myentertainment.`object`.StoragePathObject
import com.example.myentertainment.`object`.ValidationResult
import com.example.myentertainment.data.Invitation
import com.example.myentertainment.data.UserProfile
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.storage.StorageException
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import java.util.UUID
import javax.inject.Inject
import javax.inject.Named

class UserProfileActivityViewModel : ViewModel() {

    init {
        BaseApplication.baseApplicationComponent.inject(this)
    }

    @Inject
    lateinit var databaseAuth: FirebaseAuth

    @Inject
    @Named("usersReference")
    lateinit var usersReference: DatabaseReference

    @Inject
    @Named("invitationsReference")
    lateinit var invitationsReference: DatabaseReference

    @Inject
    lateinit var storageReference: StorageReference

    val user = databaseAuth.uid.toString()

    val loading = MutableLiveData<Boolean>()
    val userProfile = MutableLiveData<UserProfile?>()
    val validationResult = MutableLiveData<ValidationResult>()
    val updatingUserProfileDataSuccessful = MutableLiveData<Boolean>()
    val databaseTaskExecutionSuccessful = MutableLiveData<Boolean>()
    val profilePicture = MutableLiveData<Uri?>()


    fun changeProfilePicture(file: ByteArray) {
        loading.value = true
        val uploadTask = profilePictureReference(user).putBytes(file)
        changeProfilePicture(uploadTask)
    }

    fun changeProfilePicture(file: Uri) {
        loading.value = true
        val uploadTask = profilePictureReference(user).putFile(file)
        changeProfilePicture(uploadTask)
    }

    fun getUserProfileData(userId: String?) {
        loading.value = true
        val id = userId ?: user
        usersReference.child(id).get().addOnCompleteListener() { task ->
            if (task.isSuccessful) {
                userProfile.value = task.result?.getValue(UserProfile::class.java)
                getProfilePictureUrl(id)
            } else {
                loading.value = false
                userProfile.value = null
            }
        }
    }

    fun removeProfilePicture() {
        loading.value = true
        profilePictureReference(user).delete().addOnCompleteListener() { task ->
            if (task.isSuccessful) {
                getProfilePictureUrl(user)
            } else {
                loading.value = false
                databaseTaskExecutionSuccessful.value = false
            }
        }
    }

    fun sendInvitation(invitingUserId: String) {
        loading.value = true
        val invitationId = UUID.randomUUID().toString()
        val invitation = Invitation(invitationId, user)
        invitationsReference.child(invitingUserId).child(invitationId).setValue(invitation)
    }

    fun updateUserProfileData(userProfileData: UserProfile) {
        loading.value = true

        if (validation(userProfileData)) {
            val data = hashMapOf<String, Any>(user to userProfileData)
            usersReference.updateChildren(data).addOnCompleteListener() { task ->
                loading.value = false
                updatingUserProfileDataSuccessful.value = task.isSuccessful
            }
        }
    }

    private fun changeProfilePicture(uploadTask: UploadTask) {
        uploadTask.addOnCompleteListener() { task ->
            if (task.isSuccessful) {
                getProfilePictureUrl(user)
            } else {
                loading.value = false
                databaseTaskExecutionSuccessful.value = false
            }
        }
    }

    private fun getProfilePictureUrl(id: String) {
        profilePictureReference(id).downloadUrl
            .addOnSuccessListener {
                loading.value = false
                profilePicture.value = it

            }.addOnFailureListener {
                loading.value = false
                val errorCode = (it as StorageException).errorCode

                if (errorCode == StorageException.ERROR_OBJECT_NOT_FOUND) {
                    profilePicture.value = null
                } else {
                    databaseTaskExecutionSuccessful.value = false
                }
            }
    }

    private fun profilePictureReference(id: String): StorageReference {
        val path = StoragePathObject.PATH_PROFILE_PICTURES + "/" + id
        return storageReference.child(path)
    }

    private fun validation(userProfileData: UserProfile): Boolean {
        return if (userProfileData.username.isNullOrEmpty()) {
            loading.value = false
            validationResult.value = ValidationResult.EMPTY_VALUES
            false
        } else true
    }

}