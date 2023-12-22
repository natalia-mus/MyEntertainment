package com.example.myentertainment.viewmodel.main

import androidx.lifecycle.MutableLiveData
import com.example.myentertainment.BaseApplication
import com.example.myentertainment.data.Invitation
import com.example.myentertainment.viewmodel.UserProfileViewModel
import com.google.firebase.database.DatabaseReference
import java.util.*
import javax.inject.Inject
import javax.inject.Named

class MainActivityViewModel : UserProfileViewModel() {

    init {
        BaseApplication.baseApplicationComponent.inject(this)
    }

    @Inject
    @Named("friendsReference")
    lateinit var friendsReference: DatabaseReference

    @Inject
    @Named("invitationsReference")
    lateinit var invitationsReference: DatabaseReference

    val invitations = MutableLiveData<ArrayList<Invitation>>()


    fun acceptInvitation(invitation: Invitation) {
        if (invitation.invitingUserId != null) {
            friendsReference.child(currentUser).child(invitation.invitingUserId.toString()).setValue(invitation.invitingUserId)
            friendsReference.child(invitation.invitingUserId!!).child(currentUser).setValue(currentUser)
            removeInvitation(invitation.invitingUserId!!)
        }
    }

    fun declineInvitation(invitationId: String) {
        removeInvitation(invitationId)
    }

    fun getInvitations() {
        invitationsReference.child(currentUser).get().addOnCompleteListener { task ->
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
            val userIds = ArrayList<String?>()
            for (invitation in invitations) {
                val userId = invitation.invitingUserId
                userIds.add(userId)
            }

            getUserProfiles(userIds)
        }
    }

    fun signOut() {
        databaseAuth.signOut()
    }

    private fun parseInvitation(invitation: HashMap<String, Invitation>): Invitation {
        var invitingUserId = ""

        if (invitation.containsKey("invitingUserId")) {
            invitingUserId = invitation["invitingUserId"].toString()
        }

        return Invitation(invitingUserId)
    }

    private fun removeInvitation(invitationId: String) {
        invitationsReference.child(currentUser).child(invitationId).removeValue()
    }

}