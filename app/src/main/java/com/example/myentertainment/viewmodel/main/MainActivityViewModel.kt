package com.example.myentertainment.viewmodel.main

import androidx.lifecycle.MutableLiveData
import com.example.myentertainment.BaseApplication
import com.example.myentertainment.data.Invitation
import com.example.myentertainment.data.UserProfileData
import com.example.myentertainment.viewmodel.UserProfileViewModel
import com.google.firebase.database.DatabaseReference
import javax.inject.Inject
import javax.inject.Named

class MainActivityViewModel : UserProfileViewModel() {

    init {
        BaseApplication.baseApplicationComponent.inject(this)
    }

    @Inject
    @Named("invitationsReference")
    lateinit var invitationsReference: DatabaseReference

    val invitations = MutableLiveData<ArrayList<Invitation>>()
    val invitingUsers = MutableLiveData<ArrayList<UserProfileData>>()

    private var invitingUsersRequests = 0
    private var profilePicturesRequests = 0


    fun getInvitations() {
        invitationsReference.child(user).get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                if (task.result != null && task.result!!.value != null) {
                    val result = ArrayList<Invitation>()
                    val pendingInvitations = task.result!!.value as HashMap<String, Any>

                    for (item in pendingInvitations) {
                        val invitationObject = item.value as HashMap<String, Invitation>
                        val invitation = parseInvitation(invitationObject)
                        result.add(invitation)
                    }

                    invitations.value = result
                    getInvitingUsers()
                }

            }
        }
    }

    private fun getInvitingUsers() {
        val invitations = invitations.value

        if (invitations != null) {
            val users = ArrayList<UserProfileData>()

            for (invitation in invitations) {
                val userId = invitation.invitingUserId
                if (userId != null) {
                    invitingUsersRequests++
                    usersReference.child(userId).get().addOnCompleteListener() { task ->
                        if (task.isSuccessful && task.result != null) {
                            val userProfileData = task.result!!.getValue(UserProfileData::class.java)
                            if (userProfileData != null) { users.add(userProfileData) }
                        }

                        invitingUsersRequests--
                        if (invitingUsersRequests == 0) {
                            invitingUsers.value = users
                            //getProfilePictures()
                        }
                    }
                }
            }
        }
    }

//    private fun getProfilePictures() {
//        val pictures = java.util.HashMap<String, Uri>()
//        val usersSet = invitingUsers.value
//        if (usersSet != null) {
//            for (user in usersSet) {
//                profilePicturesRequests++
//                profilePictureReference(user.userId!!).downloadUrl.addOnCompleteListener() { task ->
//                    if (task.isSuccessful && task.result != null) {
//                        pictures.put(user.userId, task.result!!)
//                    }
//
//                    profilePicturesRequests--
//                    if (profilePicturesRequests == 0) {
//                        profilePictures.value = pictures
//                        status.value = if (users.value?.isNotEmpty() == true) SearchUsersStatus.SUCCESS else SearchUsersStatus.NO_RESULTS
//                        loading.value = false
//                    }
//                }
//
//            }
//
//        }
//    }

    fun signOut() {
        databaseAuth.signOut()
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

//    private fun parseUserProfile(userProfile: Any?): UserProfile? {
//        var result = null
//
//        if (userProfile != null) {
//            if (userProfile)
//        }
//
//        return result
//    }

}