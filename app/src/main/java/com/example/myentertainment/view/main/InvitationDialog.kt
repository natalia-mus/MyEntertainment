package com.example.myentertainment.view.main

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import com.bumptech.glide.Glide
import com.example.myentertainment.R
import com.example.myentertainment.data.Invitation
import com.example.myentertainment.data.UserProfile

class InvitationDialog(context: Context, private val invitations: ArrayList<Invitation>, private val invitingUsers: ArrayList<UserProfile>) : Dialog(context) {

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

    private fun getInvitingUser(invitingUserId: String?): UserProfile? {
        var invitingUser: UserProfile? = null
        if (invitingUserId != null && invitingUserId.isNotEmpty()) {
            for (user in invitingUsers) {
                if (user.userProfileData?.userId == invitingUserId) {
                    invitingUser = user
                    break
                }
            }
        }
        return invitingUser
    }

    private fun nextInvitation() {
        if (currentInvitationIndex == invitations.lastIndex - 1) {
            currentInvitationIndex = 0
        } else {
            currentInvitationIndex++
        }

        val invitation = invitations[currentInvitationIndex]
        val user = getInvitingUser(invitation.invitingUserId)

        if (user != null) {
            username.text = user.userProfileData!!.username
            invitingUserUsername.text = user.userProfileData.username
            invitingUserRealName.text = user.userProfileData.realName
            invitingUserLocation.text = user.userProfileData.city + ", " + user.userProfileData.country

            if (user.userProfilePicture != null) {
                val image = user.userProfilePicture
                Glide.with(context)
                    .load(image)
                    .circleCrop()
                    .placeholder(ResourcesCompat.getDrawable(context.resources, R.drawable.placeholder_user, null))
                    .into(invitingUserProfilePicture)
            }
        }
    }

}