package com.example.myentertainment.viewmodel.main

import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.myentertainment.BaseApplication
import com.example.myentertainment.data.Invitation
import com.example.myentertainment.data.UserProfile
import com.example.myentertainment.viewmodel.SearchUsersStatus
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import javax.inject.Inject
import javax.inject.Named

class MainActivityViewModel : ViewModel() {

    init {
        BaseApplication.baseApplicationComponent.inject(this)
    }

    @Inject
    lateinit var databaseAuth: FirebaseAuth

    @Inject
    @Named("invitationsReference")
    lateinit var invitationsReference: DatabaseReference

    @Inject
    @Named("usersReference")
    lateinit var usersReference: DatabaseReference

    val user = databaseAuth.uid.toString()

    val invitations = MutableLiveData<ArrayList<Invitation>>()
    val invitingUsers = MutableLiveData<ArrayList<UserProfile>>()

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
            val users = ArrayList<UserProfile>()

            for (invitation in invitations) {
                val userId = invitation.invitingUserId
                if (userId != null) {
                    invitingUsersRequests++
                    usersReference.child(userId).get().addOnCompleteListener() { task ->
                        if (task.isSuccessful && task.result != null) {
                            val userProfile = task.result!!.getValue(UserProfile::class.java)
                            if (userProfile != null) { users.add(userProfile) }
                        }

                        invitingUsersRequests--
                        if (invitingUsersRequests == 0) {
                            invitingUsers.value = users
                            getProfilePictures()
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