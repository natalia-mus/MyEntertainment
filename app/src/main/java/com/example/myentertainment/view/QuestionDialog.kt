package com.example.myentertainment.view

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View.OnClickListener
import android.widget.Button
import android.widget.TextView
import com.example.myentertainment.R

class QuestionDialog(context: Context, private val question: String, private val onConfirmButtonClickListener: OnClickListener) : Dialog(context) {

    private lateinit var confirmButton: Button
    private lateinit var declineButton: Button
    private lateinit var questionLabel: TextView

    companion object {
        fun createAndShow(context: Context, question: String, onConfirmButtonClickListener: OnClickListener) {
            QuestionDialog(context, question, onConfirmButtonClickListener).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.question_dialog)
        setView()
    }

    private fun setView() {
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        questionLabel = findViewById(R.id.question_dialog_question)
        confirmButton = findViewById(R.id.question_dialog_confirm_button)
        declineButton = findViewById(R.id.question_dialog_decline_button)

        questionLabel.text = question

        confirmButton.setOnClickListener {
            onConfirmButtonClickListener.onClick(it)
            dismiss()
        }

        declineButton.setOnClickListener {
            dismiss()
        }
    }
}