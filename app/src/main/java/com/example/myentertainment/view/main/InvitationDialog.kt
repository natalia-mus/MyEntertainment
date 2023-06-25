package com.example.myentertainment.view.main

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import com.example.myentertainment.R
import com.example.myentertainment.data.Invitation

class InvitationDialog(context: Context, private val invitations: ArrayList<Invitation>) : Dialog(context) {

    private val username: TextView by lazy { findViewById(R.id.invitation_invitingUsername) }
    private val invitingUserProfilePicture: ImageView by lazy { findViewById(R.id.user_image) }
    private val invitingUserUsername: TextView by lazy { findViewById(R.id.user_username) }
    private val invitingUserRealName: TextView by lazy { findViewById(R.id.user_realName) }
    private val invitingUserLocation: TextView by lazy { findViewById(R.id.user_location) }

    private var currentInvitationIndex = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.invitation)
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        nextInvitation()
    }

    private fun nextInvitation() {
        if (currentInvitationIndex == invitations.lastIndex - 1) {
            currentInvitationIndex = 0
        } else {
            currentInvitationIndex++
        }

        val invitation = invitations[currentInvitationIndex]
        val user = invitation.invitingUserId?.let { getInvitingUser(it) }

        if (user != null) {
            username.text = user.username
            invitingUserUsername.text = user.username
            invitingUserRealName.text = user.realName
            invitingUserLocation.text = user.city + ", " + user.country
        }
    }

}