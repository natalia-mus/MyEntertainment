package com.example.myentertainment.viewmodel.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.myentertainment.BaseApplication
import com.example.myentertainment.data.Invitation
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

    val user = databaseAuth.uid.toString()

    val invitations = MutableLiveData<ArrayList<Invitation>>()


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
                }

            }
        }
    }

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

}