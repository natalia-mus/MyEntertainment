package com.example.myentertainment.viewmodel

import android.net.Uri
import androidx.lifecycle.MutableLiveData
import com.example.myentertainment.BaseApplication
import com.example.myentertainment.`object`.ValidationResult
import com.example.myentertainment.data.Invitation
import com.example.myentertainment.data.UserProfileData
import com.google.firebase.database.DatabaseReference
import com.google.firebase.storage.UploadTask
import java.util.*
import javax.inject.Inject
import javax.inject.Named

class UserProfileActivityViewModel : UserProfileViewModel() {

    init {
        BaseApplication.baseApplicationComponent.inject(this)
    }

    @Inject
    @Named("friendsReference")
    lateinit var friendsReference: DatabaseReference

    @Inject
    @Named("invitationsReference")
    lateinit var invitationsReference: DatabaseReference

    val validationResult = MutableLiveData<ValidationResult>()
    val updatingUserProfileDataSuccessful = MutableLiveData<Boolean>()
    val updatingProfilePictureSuccessful = MutableLiveData<Boolean>()
    val sendingInvitationSuccessful = MutableLiveData<Boolean>()
    val friendshipStatus = MutableLiveData<FriendshipStatus>()


    fun changeProfilePicture(file: ByteArray) {
        val uploadTask = profilePictureReference(currentUser).putBytes(file)
        changeProfilePicture(uploadTask)
    }

    fun changeProfilePicture(file: Uri) {
        val uploadTask = profilePictureReference(currentUser).putFile(file)
        changeProfilePicture(uploadTask)
    }

    fun getFriendshipStatus(userId: String?) {
        if (userId != null && userId != currentUser) {

            // check if invitation is pending:
            invitationsReference.child(userId).child(currentUser).get().addOnCompleteListener { task ->
                if (task.isSuccessful && task.result?.value != null) {
                    friendshipStatus.value = FriendshipStatus.PENDING

                } else {
                    // check if user exists in table "friends"
                    friendsReference.child(userId).child(currentUser).get().addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            if (task.result?.value != null) {
                                friendshipStatus.value = FriendshipStatus.READY_TO_REMOVE
                            } else {
                                friendshipStatus.value = FriendshipStatus.READY_TO_INVITE
                            }
                        } else {
                            friendshipStatus.value = FriendshipStatus.UNKNOWN
                        }
                    }
                }
            }
        } else {
            friendshipStatus.value = FriendshipStatus.UNKNOWN
        }
    }

    override fun onGettingProfilePictureFailed() {
        updatingProfilePictureSuccessful.value = false
    }

    fun removeFriend(friendId: String) {
        friendsReference.child(currentUser).child(friendId).removeValue().addOnSuccessListener {
            friendsReference.child(friendId).child(currentUser).removeValue().addOnSuccessListener {
                friendshipStatus.value = FriendshipStatus.READY_TO_INVITE
            }
        }
    }

    fun removeInvitation(invitedUserId: String) {
        invitationsReference.child(invitedUserId).child(currentUser).removeValue().addOnSuccessListener {
            friendshipStatus.value = FriendshipStatus.READY_TO_INVITE
        }
    }

    fun removeProfilePicture() {
        profilePictureReference(currentUser).delete().addOnCompleteListener() { task ->
            if (task.isSuccessful) {
                getProfilePictureUrl(currentUser)
            } else {
                updatingProfilePictureSuccessful.value = false
            }
        }
    }

    fun sendInvitation(invitedUserId: String) {
        val invitation = Invitation(currentUser)

        invitationsReference.child(invitedUserId).child(currentUser).setValue(invitation).addOnCompleteListener() { task ->
            sendingInvitationSuccessful.value = task.isSuccessful
            if (task.isSuccessful) {
                friendshipStatus.value = FriendshipStatus.PENDING
            }
        }
    }

    fun updateUserProfileData(userProfileData: UserProfileData) {
        if (validation(userProfileData)) {
            val data = hashMapOf<String, Any>(currentUser to userProfileData)
            usersReference.updateChildren(data).addOnCompleteListener() { task ->
                updatingUserProfileDataSuccessful.value = task.isSuccessful
            }
        }
    }

    private fun changeProfilePicture(uploadTask: UploadTask) {
        userProfilesArray.clear()
        uploadTask.addOnCompleteListener() { task ->
            if (task.isSuccessful) {
                getProfilePictureUrl(currentUser)
            } else {
                updatingProfilePictureSuccessful.value = false
            }
        }
    }

    private fun validation(userProfileData: UserProfileData): Boolean {
        return if (userProfileData.username.isNullOrEmpty()) {
            validationResult.value = ValidationResult.EMPTY_VALUES
            false
        } else true
    }

}


enum class FriendshipStatus {
    UNKNOWN, PENDING, READY_TO_INVITE, READY_TO_REMOVE
}