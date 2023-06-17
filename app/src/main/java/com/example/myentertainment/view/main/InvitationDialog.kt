package com.example.myentertainment.view.main

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import com.example.myentertainment.R
import com.example.myentertainment.data.Invitation

class InvitationDialog(context: Context, invitationDialogs: ArrayList<Invitation>) : Dialog(context) {

    private var currentInvitationIndex = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.invitation)
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        nextInvitation()
    }

    private fun nextInvitation() {
        // todo
    }

}