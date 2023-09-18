package com.example.myentertainment.view.main

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import com.bumptech.glide.Glide
import com.example.myentertainment.R
import com.example.myentertainment.data.Invitation
import com.example.myentertainment.data.UserProfile

class InvitationDialog(
    context: Context,
    private val invitations: ArrayList<Invitation>,
    private val invitingUsers: ArrayList<UserProfile>,
    private val invitationDialogListener: InvitationDialogListener
) : Dialog(context) {

    private val username: TextView by lazy { findViewById(R.id.invitation_invitingUsername) }
    private val invitingUserProfilePicture: ImageView by lazy { findViewById(R.id.user_image) }
    private val invitingUserUsername: TextView by lazy { findViewById(R.id.user_username) }
    private val invitingUserRealName: TextView by lazy { findViewById(R.id.user_realName) }
    private val invitingUserLocation: TextView by lazy { findViewById(R.id.user_location) }
    private val acceptButton: Button by lazy { findViewById(R.id.invitation_acceptButton) }
    private val declineButton: Button by lazy { findViewById(R.id.invitation_declineButton) }
    private val laterButton: Button by lazy { findViewById(R.id.invitation_laterButton) }
    private val nextButton: ImageButton by lazy { findViewById(R.id.invitation_nextButton) }
    private val previousButton: ImageButton by lazy { findViewById(R.id.invitation_previousButton) }

    private var currentInvitationIndex = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.invitation)
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        setOnClickListeners()
        setInvitationData()
    }

    private fun decline(invitation: Invitation) {
        invitationDialogListener.onDeclineClick(invitation.id!!)
        invitations.remove(invitation)

        if (invitations.isNotEmpty()) {
            if (currentInvitationIndex > invitations.lastIndex) {
                nextInvitation()
            } else {
                setInvitationData()
            }
        } else {
            dismiss()
        }
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
        if (currentInvitationIndex >= invitations.lastIndex) {
            currentInvitationIndex = 0
        } else {
            currentInvitationIndex++
        }

        setInvitationData()
    }

    private fun previousInvitation() {
        if (currentInvitationIndex == 0) {
            currentInvitationIndex = invitations.lastIndex
        } else {
            currentInvitationIndex--
        }

        setInvitationData()
    }

    private fun setInvitationData() {
        setNavigationButtonsVisibility()

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

            if (invitation.id != null) {
                acceptButton.setOnClickListener() {
                    invitationDialogListener.onAcceptClick(invitation.id!!)
                }

                declineButton.setOnClickListener() {
                    decline(invitation)
                }
            }
        }
    }

    private fun setNavigationButtonsVisibility() {
        if (invitations.size > 1) {
            nextButton.visibility = View.VISIBLE
            previousButton.visibility = View.VISIBLE

        } else {
            nextButton.visibility = View.GONE
            previousButton.visibility = View.GONE
        }
    }

    private fun setOnClickListeners() {
        laterButton.setOnClickListener() {
            invitationDialogListener.onLaterClick()
        }

        nextButton.setOnClickListener() {
            nextInvitation()
        }

        previousButton.setOnClickListener() {
            previousInvitation()
        }
    }

}

interface InvitationDialogListener{

    fun onAcceptClick(invitationId: String)
    fun onDeclineClick(invitationId: String)
    fun onLaterClick()
}