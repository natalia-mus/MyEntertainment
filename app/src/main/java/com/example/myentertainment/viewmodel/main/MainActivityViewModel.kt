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
        val result = ArrayList<Invitation>()

        invitationsReference.child(currentUser).get().addOnSuccessListener {
            it.children.forEach {
                val child = it.getValue(Invitation::class.java)
                child?.let { invitation -> result.add(invitation) }
            }

            invitations.value = result
            getInvitingUsers()
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

    private fun removeInvitation(invitationId: String) {
        invitationsReference.child(currentUser).child(invitationId).removeValue()
    }

}