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
import kotlin.collections.HashMap

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
    val updatingProfilePictureSuccessful = MutableLiveData<Boolean>()
    val sendingInvitationSuccessful = MutableLiveData<Boolean>()
    val profilePicture = MutableLiveData<Uri?>()
    val friendshipStatus = MutableLiveData<FriendshipStatus>()


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

    fun getFriendshipStatus(userId: String?) {
        friendshipStatus.value = FriendshipStatus.UNKNOWN
        if (userId != null && userId != user) {

            // check if friendship status is pending:
            invitationsReference.child(userId).get().addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        if (task.result != null && task.result!!.value != null) {
                            val pendingInvitations = task.result!!.value as HashMap<String, Any>

                            for (item in pendingInvitations) {
                                val invitationObject = item.value as HashMap<String, Invitation>
                                val invitation = parseInvitation(invitationObject)
                                if (invitation.invitingUserId == user) {
                                    friendshipStatus.value = FriendshipStatus.PENDING
                                    break
                                }
                            }
                        }

                    } else {
                        // TODO
                    }
                }

            // TODO - check if current user exists in table "friends"
        }
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
                updatingProfilePictureSuccessful.value = false
            }
        }
    }

    fun sendInvitation(invitedUserId: String) {
        loading.value = true
        val invitationId = UUID.randomUUID().toString()
        val invitation = Invitation(invitationId, user)

        invitationsReference.child(invitedUserId).child(invitationId).setValue(invitation).addOnCompleteListener() { task ->
            loading.value = false
            sendingInvitationSuccessful.value = task.isSuccessful
        }
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
                updatingProfilePictureSuccessful.value = false
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
                    updatingProfilePictureSuccessful.value = false
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

    private fun parseInvitation(invitation: HashMap<String, Invitation>): Invitation {
        var id = ""
        var invitingUserId = ""

        if (invitation.containsKey("id")) {
            id = invitation["id"].toString()
        }

        if (invitation.containsKey("invitingUserId")) {
            invitingUserId = invitation["invitingUserId"].toString()
        }

        return Invitation(id, invitingUserId)
    }

}


enum class FriendshipStatus {
    UNKNOWN, PENDING, READY_TO_INVITE, READY_TO_UNFRIEND
}