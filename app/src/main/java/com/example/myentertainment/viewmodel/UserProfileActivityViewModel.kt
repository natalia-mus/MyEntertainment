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
    @Named("invitationsReference")
    lateinit var invitationsReference: DatabaseReference

    val validationResult = MutableLiveData<ValidationResult>()
    val updatingUserProfileDataSuccessful = MutableLiveData<Boolean>()
    val sendingInvitationSuccessful = MutableLiveData<Boolean>()
    val friendshipStatus = MutableLiveData<FriendshipStatus>()


    fun changeProfilePicture(file: ByteArray) {
        val uploadTask = profilePictureReference(user).putBytes(file)
        changeProfilePicture(uploadTask)
    }

    fun changeProfilePicture(file: Uri) {
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

    fun removeProfilePicture() {
        profilePictureReference(user).delete().addOnCompleteListener() { task ->
            if (task.isSuccessful) {
                getProfilePictureUrl(user)
            } else {
                updatingProfilePictureSuccessful.value = false
            }
        }
    }

    fun sendInvitation(invitedUserId: String) {
        val invitationId = UUID.randomUUID().toString()
        val invitation = Invitation(invitationId, user)

        invitationsReference.child(invitedUserId).child(invitationId).setValue(invitation).addOnCompleteListener() { task ->
            sendingInvitationSuccessful.value = task.isSuccessful
        }
    }

    fun updateUserProfileData(userProfileData: UserProfileData) {
        if (validation(userProfileData)) {
            val data = hashMapOf<String, Any>(user to userProfileData)
            usersReference.updateChildren(data).addOnCompleteListener() { task ->
                updatingUserProfileDataSuccessful.value = task.isSuccessful
            }
        }
    }

    private fun changeProfilePicture(uploadTask: UploadTask) {
        uploadTask.addOnCompleteListener() { task ->
            if (task.isSuccessful) {
                getProfilePictureUrl(user)
            } else {
                updatingProfilePictureSuccessful.value = false
            }
        }
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

    private fun validation(userProfileData: UserProfileData): Boolean {
        return if (userProfileData.username.isNullOrEmpty()) {
            validationResult.value = ValidationResult.EMPTY_VALUES
            false
        } else true
    }

}


enum class FriendshipStatus {
    UNKNOWN, PENDING, READY_TO_INVITE, READY_TO_UNFRIEND
}